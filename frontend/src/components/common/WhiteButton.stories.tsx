import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import WhiteButton from './whiteButton';
import { WhiteButtonProps } from './whiteButton';

// 스토리북에서 사용할 메타 정보
export default {
  title: 'Components/WhiteButton',
  component: WhiteButton,
  argTypes: {
    w: { control: 'text' },
    h: { control: 'text' },
    text: { control: 'text' },
    borderColor: { control: 'color' },  // 테두리 색깔 선택
    textColor: { control: 'color' },    // 텍스트 색깔 선택
  },
} as Meta<typeof WhiteButton>;

// 기본 템플릿 생성
const Template: StoryFn<WhiteButtonProps> = (args) => <WhiteButton {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  w: '100px',
  h: '40px',
  text: '버튼',
  borderColor: '#9A75BF',
  textColor: '#9A75BF',
};

// 너비가 더 넓은 버튼 스토리
export const LargeButton = Template.bind({});
LargeButton.args = {
  w: '200px',
  h: '50px',
  text: '큰 버튼',
  borderColor: '#9A75BF',
  textColor: '#9A75BF',
};

// 사용자 정의 색상을 적용한 버튼 스토리
export const CustomColorsButton = Template.bind({});
CustomColorsButton.args = {
  w: '150px',
  h: '45px',
  text: '커스텀 버튼',
  borderColor: '#FF5733',
  textColor: '#33FF57',
};
