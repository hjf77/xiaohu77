/*
 * @Author: sky.li
 * @LastEditors: sky.li
 * @Description:
 * @可以输入预定的版权声明、个性签名、空行等
 */
import pageCom from "./page.vue";
import card from "./card.vue";
import crud from "./crud.vue";
import button from "./button.vue";
import form from "./form.vue";
import dialog from "./dialog.vue";
import select from "./select.vue";
import radio from "./radio.vue";
import checkbox from "./checkbox.vue";
import switchs from "./switchs.vue";
import inputNumber from "./inputNumber.vue";
import slider from "./slider.vue";
import pagination from "./pagination.vue";

import Vue from "vue";
import getProps from "../utils/getProps";

const renderCom = {
    props: {
        schema: {
            type: Object,
            default: () => ({})
        }
    },
    render(h) {
        return render(this.schema, h);
    }
};

const comList = {
    page: pageCom,
    card,
    crud,
    button, form,
    dialog, select, radio, checkbox,switchs,inputNumber,slider,pagination,
    render: renderCom
};
Object.entries(comList).forEach(([key, val]) => {
    Vue.component("pagex-" + key, val);
});

function render(schema, h) {
    let child = [];
    if (schema.type === undefined) {
        return undefined
    }
    const props = getProps(schema);
    const layout = "pagex-" + schema.type;
    if (Array.isArray(schema.body)) {
        child = schema.body.map(item => render(item, h));
    } else if (!schema.body) {
        child = [];
    } else {
        child = [render(schema.body, h)];
    }



    return h(layout, { props }, child);
}


const renderFun = schema => {
    return {

        render(h) {
            return render(schema, h);
        }
    };
};
export default renderFun