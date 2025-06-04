import { KoreanRegion } from "@/domain/KoreanRegion";

export class WelfareCenter {

    public welfareCenterId : Number;
    public name : String;
    public region : KoreanRegion;

    public constructor(data : any) {
        this.welfareCenterId = data.welfareCenterId;
        this.name = data.name;
        this.region = KoreanRegion.getRegionByKey(data.region);
    }

}