export class Member {

  public nickname : string;
  public role: string;

  constructor(data: any) {
    this.nickname = data.nickname as string;
    this.role = data.role as string;
  }
}