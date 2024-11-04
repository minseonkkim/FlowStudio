import React from 'react';
import Search from './Search';

export default {
  title: 'Components/Search',  // 스토리북에서 보여질 이름
  component: Search,  // 사용할 컴포넌트
};

// 기본 템플릿 생성
const Template = () => <Search onSearchChange={function (): void {
  throw new Error('Function not implemented.');
} }/>;

// 기본 스토리
export const Default = Template.bind({});

