package org.openpaas.servicebroker.kubernetes.service;

import org.openpaas.servicebroker.model.Plan;
import org.springframework.data.util.Pair;

/**
 * Kubernetes를 관리하기 위한 Service의 인터페이스이다.
 *
 * @author Hyungu Cho
 * @since 2018/07/25
 * @version 20180725
 */
public interface KubernetesService {
    /**
     * Kubernetes에 지정한 이름으로 Namespace를 생성한다.
     * Namespace가 제대로 생성될 경우 true를 반환, 아닐 경우 false를 반환한다.
     * @param namespace Namespace의 이름
     * @param accountName Namespace의 관리자의 계정의 이름 (ID)
     * @param plan Namespace에 대한 제한을 걸 수 있는 플랜
     * @return
     */
    boolean createNamespace(String namespace, String accountName, Plan plan);

    /**
     * Kubernetes에서 사용했던 Namespace를 지운다.
     * 정상적으로 삭제된 경우 true를 반환, 아닐경우 false를 반환한다.
     * @param namespace Namespace의 이름
     * @return
     */
    boolean deleteNamespace(String namespace);

    /**
     * Namespace가 존재하는지 확인한다.
     * @param namespace
     * @return true if exists
     */
    boolean existsNamespace(String namespace);

    /**
     * Namespace에 있는 관리자 계정의 이름을 변경한다. (엑세스 토큰도 같이 변경)
     * @param namespace
     * @param oldName
     * @param newName
     * @return
     */
    boolean changeAccountName(String namespace, String oldName, String newName);

    /**
     * Namespace에 있는 관리자 계정의 엑세스 토큰을 변경한다. (토큰 값은 지정할 수 없다.)
     * 이 메소드는 계정에 대해 새로운 Secret을 만드는 동작과 같다.
     * @param namespace
     * @param accountName
     * @return
     */
    Pair<Boolean, String> changeAccountAccessToken( String namespace, String accountName);

    /**
     * Namespace의 Plan을 변경한다. (Plan 정보는 외부로부터 전달받는다.)
     * @param namespace
     * @param plan
     * @return
     */
    boolean changeNamespacePlan(String namespace, Plan plan);
}
