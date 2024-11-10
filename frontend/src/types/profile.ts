export interface UserInfos {
  id: number;
  username: string;
  nickname: string;
  profileImage: string;
}


export interface ApiKeys {
  openAiKey: string | null;
  claudeKey: string | null;
  geminiKey: string | null;
  clovaKey: string | null;
}