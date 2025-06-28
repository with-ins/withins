

export class FetchResponse {

    readonly status: number;
    readonly message: string;
    data: any;

    constructor(statusCode: number, message: string, data: any) {
        this.status = statusCode;
        this.message = message;
        this.data = data;
    }
}

