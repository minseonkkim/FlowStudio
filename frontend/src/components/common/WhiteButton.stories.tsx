import { Meta, StoryFn } from '@storybook/react';
import WhiteButton from './whiteButton';
import { WhiteButtonProps } from './whiteButton';

// 스토리북에서 사용할 메타 정보
export default {
  title: 'Components/WhiteButton',
  component: WhiteButton,
  argTypes: {
    text: { control: 'text' },
  },
} as Meta<typeof WhiteButton>;

// 기본 템플릿 생성
const Template: StoryFn<WhiteButtonProps> = (args) => <WhiteButton {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  text: '버튼'
};
