import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import PurpleButton, { PurpleButtonProps } from './PurpleButton';

// 스토리북에서 사용할 메타 정보
export default {
  title: 'Components/PurpleButton',
  component: PurpleButton,
  argTypes: {
    w: { control: 'text' },
    h: { control: 'text' },
    text: { control: 'text' },
  },
} as Meta<typeof PurpleButton>;

// 기본 템플릿 생성
const Template: StoryFn<PurpleButtonProps> = (args) => <PurpleButton {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  w: '100px',
  h: '40px',
  text: '파일 추가',
};

// 너비가 더 넓은 버튼 스토리
export const LargeButton = Template.bind({});
LargeButton.args = {
  w: '200px',
  h: '50px',
  text: '업로드',
};
