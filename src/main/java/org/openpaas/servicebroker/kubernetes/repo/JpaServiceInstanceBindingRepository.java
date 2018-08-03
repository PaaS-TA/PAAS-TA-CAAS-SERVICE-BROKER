package org.openpaas.servicebroker.kubernetes.repo;

import java.util.List;
import java.util.Map;

import org.openpaas.servicebroker.kubernetes.model.JpaServiceInstanceBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * CaaS service broker에서는 bind/unbind를 쓰지 않기로 했으나 JPA Repository 클래스는 남겨둡니다.
 *
 * @author Hyungu Cho
 * @since 2018.07.24
 * @version 20180724
 */
@Repository
public interface JpaServiceInstanceBindingRepository extends JpaRepository<JpaServiceInstanceBinding, String>{
    List<JpaServiceInstanceBinding> findByServiceInstanceId(String serviceInstanceId);

    //@Query(value = "SELECT b FROM JpaServiceInstanceBinding b JOIN b.credentials c " + "WHERE ( KEY(c) = :username)", nativeQuery = true)
    @Query(value = "SELECT username FROM dba_users where username=?1",nativeQuery = true)
    List<Map<String,Object>> findByUsername(String userId);

    @Query(value = "select '*' from dba_profiles where profile = ?1", nativeQuery = true)
    List<Map<String,Object>> isExistProfile(String planIdName);
}
