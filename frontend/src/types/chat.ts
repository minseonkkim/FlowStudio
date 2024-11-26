export interface ChatList {
  id: number;
  title: string;
}

export interface getChatListData {
  id: number;
  title: string;
  thumbnail: number;
  totalCount: number;
  chats: ChatList[]; 
}

export interface getChatDetailList {
  id: number;
  title: string;
  messageList: string
}

export interface Message {
  text: string;
  sender: "user" | "server";
 
};