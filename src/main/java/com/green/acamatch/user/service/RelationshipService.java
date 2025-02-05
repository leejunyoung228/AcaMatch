package com.green.acamatch.user.service;

import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.RelationshipErrorCode;
import com.green.acamatch.config.security.AuthenticationFacade;
import com.green.acamatch.entity.user.Relationship;
import com.green.acamatch.entity.user.RelationshipId;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.UserUtils;
import com.green.acamatch.user.model.RelationshipReq;
import com.green.acamatch.user.model.RelationshipRes;
import com.green.acamatch.user.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final UserUtils userUtils;
    private final RelationshipRepository relationshipRepository;

    public int acceptRelationship(Long studentId) {
        User parent = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        User student = userUtils.findUserById(studentId);
        Relationship relationship = relationshipRepository.findRelationshipByParentAndStudent(parent, student)
                .orElseThrow(() -> new CustomException(RelationshipErrorCode.REQUEST_RELATIONSHIP_NOT_FOUND));
        relationship.setCertification(1);
        relationshipRepository.save(relationship);
        return 1;
    }

    public List<RelationshipRes> getRelationships(int type) {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        List<Relationship> relationships;
        if (type == 1) {
            relationships = relationshipRepository.findRelationshipsByParent(user);
            return getStudentInfoByRelationships(relationships);
        } else if (type == 2) {
            relationships = relationshipRepository.findRelationshipsByStudent(user);
            return getParentInfoByRelationships(relationships);
        } else {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
    }

    public int addRelationship(RelationshipReq req) {
        User parent = userUtils.findUserByEmail(req.getEmail());

        RelationshipId relationshipId = new RelationshipId();
        relationshipId.setParentsId(parent.getUserId());
        relationshipId.setStudentId(AuthenticationFacade.getSignedUserId());

        Relationship relationship = new Relationship();
        relationship.setId(relationshipId);
        relationship.setParent(parent);
        relationship.setStudent(userUtils.findUserById(AuthenticationFacade.getSignedUserId()));
        relationship.setCertification(0);

        relationshipRepository.save(relationship);
        return 1;
    }

    public int deleteRelationshipRequest(RelationshipReq req) {
        User student = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        User parent = userUtils.findUserByEmail(req.getEmail());
        Relationship relationship = relationshipRepository.findRelationshipByParentAndStudentAndCertification(parent, student, 0)
                .orElseThrow(() -> new CustomException(RelationshipErrorCode.REQUEST_RELATIONSHIP_NOT_FOUND));
        relationshipRepository.delete(relationship);
        return 1;
    }

    private List<RelationshipRes> getStudentInfoByRelationships(List<Relationship> relationships) {
        return relationships.stream()
                .map(relationship -> RelationshipRes.builder()
                        .userId(relationship.getStudent().getUserId())
                        .email(relationship.getStudent().getEmail())
                        .birth(relationship.getStudent().getBirth())
                        .name(relationship.getStudent().getName())
                        .phone(relationship.getStudent().getPhone())
                        .certification(relationship.getCertification())
                        .createdAt(relationship.getCreatedAt())
                        .updatedAt(relationship.getUpdatedAt())
                        .userPic(relationship.getStudent().getUserPic())
                        .build())
                .toList();
    }

    private List<RelationshipRes> getParentInfoByRelationships(List<Relationship> relationships) {
        return relationships.stream()
                .map(relationship -> RelationshipRes.builder()
                        .userId(relationship.getParent().getUserId())
                        .email(relationship.getParent().getEmail())
                        .birth(relationship.getParent().getBirth())
                        .name(relationship.getParent().getName())
                        .phone(relationship.getParent().getPhone())
                        .certification(relationship.getCertification())
                        .createdAt(relationship.getCreatedAt())
                        .updatedAt(relationship.getUpdatedAt())
                        .userPic(relationship.getParent().getUserPic())
                        .build())
                .toList();
    }
}
