
export class Member {

  public nickname : string;

  constructor(data: any) {
    this.nickname = data.nickname as string;
  }
}