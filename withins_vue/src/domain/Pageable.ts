
export class Pageable<T> {

    public totalElements : Number;
    public totalPages : Number;
    public pageNumber : Number;
    public condition : Object = {};
    public content : Array<T> = [];

    private constructor(data : any) {
        this.totalElements = data.page.totalElements;
        this.totalPages = data.page.totalPages;
        this.pageNumber = data.page.pageNumber;
        this.condition = data.condition;
    }
    public static of<T>(data : any) : Pageable<T> {
        return new  Pageable<T>(data);
    }

    public static empty<T>() : Pageable<T> {
        return new Pageable<T>(
            {
                page: {
                    totalElements: 0,
                    totalPages: 0,
                    pageNumber: 0,
                },
                condition: {}
            }
        );
    }


    public setContent(ts : Array<T>) {
        this.content = ts;
    }

}