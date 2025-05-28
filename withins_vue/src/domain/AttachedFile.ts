
export class AttachedFile {
    public fileName : String;
    public filePath : String;

    public constructor(data : any) {
        this.fileName = data.fileName;
        this.filePath = data.filePath;
    }
}