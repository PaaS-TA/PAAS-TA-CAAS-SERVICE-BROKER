package org.openpaas.servicebroker.model.fixture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openpaas.servicebroker.model.Plan;

public class PlanFixture {

    public static List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<Plan>();
        plans.add(getPlanOne());
        plans.add(getPlanTwo());
        plans.add(getPlanThree());
        return plans;
    }
        
    public static Plan getPlanOne() {
        //return new Plan("plan-one-id", "Plan One", "Description for Plan One");
    	return new Plan("f02690d6-6965-4756-820e-3858111ed674", "caas-plan1", "2 CPUs, 2GB Memory, 10GB Disk (free)", getPlanMetadata("A"), false, 2, "2GB", "10GB", 1);
    }

	public static Plan getPlanTwo() {
		return new Plan("a5213929-885f-414a-801f-c66ddb5e48f1", "caas-plan2", "4 CPUs, 6GB Memory, 20GB Disk (paid)", getPlanMetadata("B"), false, 4, "6GB", "20GB", 2);
    }
    
    public static Plan getPlanThree() {
    	return new Plan("7151c7ea-4fb7-4dd9-b3c6-a36a726165d2", "caas-plan3", "8 CPUs, 12GB Memory, 40GB Disk (paid)", getPlanMetadata("B"), false, 8, "12GB", "40GB", 3);
    }
    
    /**
     * Costs, bullets 정보를 포함한 Plan metadata 객체를 생성
     * @param planType
     * @return Map&lt;String, Object&gt;
     */
    public static Map<String, Object> getPlanMetadata(String planType) {
        Map<String, Object> planMetadata = new HashMap<>();
        planMetadata.put("costs", getCosts(planType));
        planMetadata.put("bullets", getBullets(planType));

        return planMetadata;
    }

    /**
     * Plan의 Costs 정보를 Map 객체의 리스트 형태로 반환
     * @param planType
     * @return Map&lt;String, Object&gt;
     */
    public static List<Map<String, Object>> getCosts(String planType) {
        Map<String, Object> costsMap = new HashMap<>();
        Map<String, Object> amount = new HashMap<>();

        switch (planType) {
            case "A":
                amount.put("usd", 0.0);
                costsMap.put("amount", amount);
                costsMap.put("unit", "FREE");
                break;
            case "B":
                amount.put("usd", 5.0);
                costsMap.put("amount", amount);
                costsMap.put("unit", "MONTHLY");
                break;
        }

        return Collections.singletonList(costsMap);
    }

    /**
     * Plan의 Bullets 정보를 담은 객체를 반환
     * @param planType
     * @return List&lt;String&gt;
     */
    public static List<String> getBullets(String planType) {
        if (planType.equals("A")) {
            return Arrays.asList("2 CPUs", "2GB Memory", "10GB Disk");
        } else if (planType.equals("B")) {
            return Arrays.asList("4 CPUs", "6GB Memory", "20GB Disk");
        }
        return Arrays.asList("2 CPUs", "2GB Memory", "10GB Disk");
    }
    
}
