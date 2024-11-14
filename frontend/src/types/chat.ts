export interface ChatList {
  id: number;
  title: string;
}

export interface getChatListData {
  id: number;
  title: string;
  thumbnail: number;
  chats: ChatList[]; 
}
