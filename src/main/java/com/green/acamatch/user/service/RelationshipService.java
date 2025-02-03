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
import com.green.acamatch.user.model.UserInfo;
import com.green.acamatch.user.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final UserUtils userUtils;
    private final RelationshipRepository relationshipRepository;

    public List<UserInfo> getRequiredRelationships() {
        List<Relationship> relationships = relationshipRepository
                .findRelationshipsByParentAndCertification(
                        userUtils.findUserById(AuthenticationFacade.getSignedUserId()),
                        0
                );
        return getUserInfoByRelationships(relationships);
    }

    public int acceptRelationship(Long studentId) {
        User parent = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        User student = userUtils.findUserById(studentId);
        Relationship relationship = relationshipRepository.findRelationshipByParentAndStudent(parent, student)
                .orElseThrow(() -> new CustomException(RelationshipErrorCode.REQUEST_RELATIONSHIP_NOT_FOUND));
        relationship.setCertification(1);
        relationshipRepository.save(relationship);
        return 1;
    }

    public List<UserInfo> getRelationships(int type) {
        User user = userUtils.findUserById(AuthenticationFacade.getSignedUserId());
        List<Relationship> relationships;
        if (type == 1) {
            relationships = relationshipRepository.findRelationshipsByParentAndCertification(user, 1);
        } else if (type == 2) {
            relationships = relationshipRepository.findRelationshipsByStudentAndCertification(user, 1);
        } else {
            throw new CustomException(CommonErrorCode.INVALID_PARAMETER);
        }
        return getUserInfoByRelationships(relationships);
    }

    public int addRelationship(RelationshipReq req) {
        User parent = userUtils.findUserByEmail(req.getEmail());

        RelationshipId relationshipId = new RelationshipId();
        relationshipId.setParentsId(parent.getUserId());
        relationshipId.setStudentId(AuthenticationFacade.getSignedUserId());

        Relationship relationship = new Relationship();
        relationship.setId(relationshipId);
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

    private List<UserInfo> getUserInfoByRelationships(List<Relationship> relationships) {
        return relationships.stream()
                .map(relationship -> UserInfo.builder()
                        .userId(relationship.getStudent().getUserId())
                        .email(relationship.getStudent().getEmail())
                        .birth(relationship.getStudent().getBirth())
                        .name(relationship.getStudent().getName())
                        .build())
                .toList();
    }

}
