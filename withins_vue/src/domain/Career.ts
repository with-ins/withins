
export class Career {
    public careerId : number;
    public imageUrl : string;
    public summary : string;
    public experience : string;
    public tags : Array<string> = [];

    public constructor(data : any) {
        this.careerId = data.careerId;
        this.imageUrl = data.imageUrl;
        this.summary = data.summary;
        this.experience = data.experience;
        this.tags = data.tags;
    }

}