
export class Pageable<T> {

    public totalElements : Number;
    public totalPages : Number;
    public pageNumber : Number;
    public condition : Object = {};
    public content : Array<T> = [];

    public constructor(data : any) {
        this.totalElements = data.page.totalElements;
        this.totalPages = data.page.totalPages;
        this.pageNumber = data.page.pageNumber;
        this.condition = data.condition;
    }


    public setContent(ts : Array<T>) {
        this.content = ts;
    }

}