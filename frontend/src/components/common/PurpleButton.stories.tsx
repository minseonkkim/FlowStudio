import { Meta, StoryFn } from '@storybook/react';
import PurpleButton, { PurpleButtonProps} from './PurpleButton';
import ColorButton from './PurpleButton';

// 스토리북에서 사용할 메타 정보
export default {
  title: 'Components/ColorButton',
  component: ColorButton,
  argTypes: {
    text: { control: 'text' },
  },
} as Meta<typeof PurpleButton>;

// 기본 템플릿 생성
const Template: StoryFn<PurpleButtonProps> = (args) => <ColorButton {...args} />;

// 기본 스토리
export const Default = Template.bind({});
Default.args = {
  text: '버튼',
};
