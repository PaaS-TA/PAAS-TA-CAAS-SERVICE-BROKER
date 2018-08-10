package org.openpaas.servicebroker.kubernetes.repo;

import org.openpaas.servicebroker.kubernetes.model.JpaAdminToken;
import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Service Instance JPA Repository 클래스
 * @author Hyerin
 * @since 2018.07.24
 * @version 20180725
 */
@Repository
public interface JpaAdminTokenRepository extends JpaRepository<JpaAdminToken, String>{

}
