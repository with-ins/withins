import {WelfareCenter} from "@/domain/WelfareCenter";


export class News {
    public newsId: number;
    public title : string;
    public welfareCenter : WelfareCenter;
    public type : string;
    public link : string;
    public createdAt : Date;

    public constructor(data : any) {
        this.newsId = data.id;
        this.title = data.title;
        this.type = data.type;
        this.link = data.link;
        this.welfareCenter = new WelfareCenter(data.welfareCenter);
        this.createdAt = new Date(data.createdAt);
    }

    public getCreateAt() : string {
        const month = (this.createdAt.getMonth() + 1).toString().padStart(2, '0');
        const day = (this.createdAt.getDate()).toString().padStart(2, '0');
        // 년 단위
        return `${this.createdAt.getFullYear()}.${month}.${day}`;
    }
}