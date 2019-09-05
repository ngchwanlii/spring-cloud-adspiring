package com.adspiring.ad.index.adunit;

import com.adspiring.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;


    public void update(AdUnitObject newObject) {
        if (newObject.getUnitId() != null) {
            this.unitId = newObject.getUnitId();
        }
        if (newObject.getUnitStatus() != null) {
            this.unitStatus = newObject.getUnitStatus();
        }
        if (newObject.getPositionType() != null) {
            this.positionType = newObject.getPositionType();
        }
        if (newObject.getPlanId() != null) {
            this.planId = newObject.getPlanId();
        }
        if (newObject.getAdPlanObject() != null) {
            this.adPlanObject = newObject.getAdPlanObject();
        }
    }

    private static boolean isScreen(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.SCREEN) > 0;
    }

    private static boolean isCard(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.CARD) > 0;
    }

    private static boolean isCardMiddle(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.CARD_MIDDLE) > 0;
    }

    private static boolean isCardPause(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.CARD_PAUSE) > 0;
    }

    private static boolean isCardPost(int positionType) {
        return (positionType & AdUnitConstants.POSITION_TYPE.CARD_POST) > 0;
    }

    public static boolean isAdSlotTypeMatch(int adSlotPositionType, int positionType) {
        switch (adSlotPositionType) {
            case AdUnitConstants.POSITION_TYPE.SCREEN:
                return isScreen(positionType);
            case AdUnitConstants.POSITION_TYPE.CARD:
                return isCard(positionType);
            case AdUnitConstants.POSITION_TYPE.CARD_MIDDLE:
                return isCardMiddle(positionType);
            case AdUnitConstants.POSITION_TYPE.CARD_PAUSE:
                return isCardPause(positionType);
            case AdUnitConstants.POSITION_TYPE.CARD_POST:
                return isCardPost(positionType);
            default:
                return false;
        }
    }


}
