import {WelfareCenter} from "@/domain/WelfareCenter";

export class Recruit {
    public recruitId: Number;
    public title : String;
    public organization : WelfareCenter;
    private createAt : Date;
    private deadline : Date;

    public constructor(data : any) {
        this.recruitId = data.recruitId;
        this.title = data.title;
        this.organization = new WelfareCenter(data.organization);
        this.createAt = new Date(data.createAt);
        this.deadline = new Date(data.deadline);
    }

    public getDeadline() : String {
        const year = this.deadline.getFullYear();
        // getMonth()는 0부터 시작하므로 1을 더해줍니다
        const month = String(this.deadline.getMonth() + 1);
        const day = String(this.deadline.getDate());

        return `~ ${year}. ${month}. ${day} 까지`;
    }

    public getCreateAt() : String {
        const now = new Date();
        const diffInSeconds = Math.floor((now.getTime() - this.createAt.getTime()) / 1000);

        // 초 단위
        if (diffInSeconds < 60) {
            return diffInSeconds <= 5 ? '방금 전 등록' : `${diffInSeconds}초 전 등록`;
        }

        // 분 단위 (1시간 미만)
        const diffInMinutes = Math.floor(diffInSeconds / 60);
        if (diffInMinutes < 60) {
            return `${diffInMinutes}분 전 등록`;
        }

        // 시간 단위 (1일 미만)
        const diffInHours = Math.floor(diffInMinutes / 60);
        if (diffInHours < 24) {
            return `${diffInHours}시간 전 등록`;
        }

        // 일 단위 (30일 미만)
        const diffInDays = Math.floor(diffInHours / 24);
        if (diffInDays < 30) {
            return `${diffInDays}일 전 등록`;
        }

        // 월 단위 (12개월 미만)
        const diffInMonths = Math.floor(diffInDays / 30);
        if (diffInMonths < 12) {
            return `${diffInMonths}달 전 등록`;
        }

        // 년 단위
        const diffInYears = Math.floor(diffInMonths / 12);
        return `${diffInYears}년 전 등록`;
    }



}