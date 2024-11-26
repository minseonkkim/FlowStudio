export interface UserInfo {
  id: number;
  username: string;
  nickname: string;
  profileImage: string;
}


export interface ApiKeys {
  openAiKey: string;
  claudeKey: string;
  // geminiKey: string;
  // clovaKey: string;
}

export interface TokenUsage {
  id: number;
  tokenUsage: number;
  date: string;
}
