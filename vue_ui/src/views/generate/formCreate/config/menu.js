import radio from './rule/radio';
import checkbox from './rule/checkbox';
import input from './rule/input';
import number from './rule/number';
import select from './rule/select';
import _switch from './rule/switch';
import slider from './rule/slider';
import time from './rule/time';
import date from './rule/date';
import cascader from './rule/cascader';
import upload from './rule/upload';
import transfer from './rule/transfer';
import tree from './rule/tree';
import editor from './rule/editor';

export default function createMenu() {
    return [
        {
            name: 'main',
            title: '表单组件',
            list: [
                input, editor, number, date, time, select, cascader, tree, radio, checkbox, _switch, slider, transfer, upload, /*rate, color*/
            ]
        }
    ];
}
