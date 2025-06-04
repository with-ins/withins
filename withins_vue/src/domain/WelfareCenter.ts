import { KoreanRegion } from "@/domain/KoreanRegion";

export class Organization {

    public organizationId : Number;
    public name : String;
    public region : KoreanRegion;

    public constructor(data : any) {
        this.organizationId = data.organizationId;
        this.name = data.name;
        this.region = KoreanRegion.getRegionByKey(data.region);
    }

}