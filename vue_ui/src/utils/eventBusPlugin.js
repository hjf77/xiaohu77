
const EVENT_BUS_LIST = 'eventBusList'

let xhbEventBUs = () => {}

xhbEventBUs.install = function(Vue) {
    Vue.mixin({
        beforeCreate() {
            // 如果组件内设置了 vueMeta 信息
            if (this.$options[EVENT_BUS_LIST] !== undefined) {
                this.$options.eventBusList.forEach(item => {
                    const eventName = this.$options.name + item.name
                    this.$EventBus.$on(eventName, (params) => {
                        item.callback(this, params)
                    })
                })
            }
        },
        beforeDestroy() {
            if (this.$options[EVENT_BUS_LIST] !== undefined) {
                this.$options.eventBusList.forEach(item => {
                  const eventName = this.$options.name + item.name
                    this.$EventBus.$off(eventName)
                })
            }
        }
        // created () {
        //     renderServerMetaInfo(this.$ssrContext, this.$metaInfo)
        // },
        // beforeMount() {
        //     // 在组件挂载到 dom 之前更新 meta 信息
        //     if (this._hasMetaInfo) {
        //         updateMetaInfo(this.$metaInfo)
        //     }
        // },
        // mounted() {
        //     // dom 挂载之后 继续监听 meta 信息。如果发生变化，继续更新
        //     if (this._hasMetaInfo) {
        //         this.$watch('$metaInfo', () => {
        //             updateMetaInfo(this.$metaInfo)
        //         })
        //     }
        // },
        // activated() {
        //     if (this._hasMetaInfo) {
        //         // keep-alive 组件激活时调用
        //         updateMetaInfo(this.$metaInfo)
        //     }
        // },
        // deactivated() {
        //     if (this._hasMetaInfo) {
        //         // keep-alive 组件停用时调用。
        //         updateMetaInfo(this.$metaInfo)
        //     }
        // }
    })
}

export default xhbEventBUs
