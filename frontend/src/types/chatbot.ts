export interface ChatFlowData {
  title: string;
  description: string;
  thumbnail: string;
  categoryIds: number[];
}

interface Author {
  id: number;
  username: string;
  nickname: string;
  profileImage: string;
}

interface Category {
  categoryId: number;
  name: string;
}

export interface ChatFlow {
  chatFlowId: number;
  title: string;
  description: string;
  author: Author;
  thumbnail: string;
  categories: Category[];
  public: boolean;
}

export interface SharedChatFlow extends ChatFlow {
  shareNum: number;
}
 