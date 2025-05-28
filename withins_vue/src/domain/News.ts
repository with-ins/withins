import { Organization } from "@/domain/Organization";


export class News {
    public newsId: number;
    public title : string;
    public organization : Organization;
    public type : string;
    public link : string;
    public createdAt : Date;

    public constructor(data : any) {
        this.newsId = data.id;
        this.title = data.title;
        this.type = data.type;
        this.link = data.link;
        this.organization = new Organization(data.organization);
        this.createdAt = new Date(data.createdAt);
    }

    public getCreateAt() : string {
        const month = (this.createdAt.getMonth() + 1).toString().padStart(2, '0');
        const day = (this.createdAt.getDate()).toString().padStart(2, '0');
        // 년 단위
        return `${this.createdAt.getFullYear()}.${month}.${day}`;
    }
}