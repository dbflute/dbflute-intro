<ui-list>
  <ul ref="items" class="ui divided items segment">
    <li class="item" each="{ item in state.items }">
      <yield/>
    </li>
  </ul>

  <style>
    /* sortable="{ true }" と指定した場合、タグに sortable という属性が付与されているので下記のstyleが反映される */
    ui-list[sortable] > .items > .item:hover {
      cursor: grab;
    }

    ui-list > .items > .item.sorted {
      background: #C8EBFB;
    }

    ui-list > .items > .item.dragging {
      background: #FFFFFF;
    }
  </style>

  <script>
    import Sortable from 'sortablejs'

    // =======================================================================================
    //                                                                              Properties
    //                                                                              ==========
    let self = this

    self.props = {
      // sortable="{ true }" と指定した場合、opts.sortableに"sortable"という文字列が渡ってくるのでbooleanに変換しておく
      sortable: !!self.opts.sortable,
      onSorted: self.opts.onsorted,
      options: self.opts.options
    }

    self.state = {
      items: self.opts.items
    }

    // =======================================================================================
    //                                                                              Life Cycle
    //                                                                              ==========
    self.on('mount', () => {
      if (self.props.sortable) {
        // SortableJSによるソート機能を対象のDOM要素にbindする
        Sortable.create(self.refs.items, self.prepareSortableOptions())
      }
    })

    self.on('update', () => {
      self.state = {
        items: self.opts.items
      }
    })

    // =======================================================================================
    //                                                                               Functions
    //                                                                               =========
    /**
     * SortableJSのオプションを構築します
     * - ref: https://github.com/SortableJS/Sortable#options
     */
    self.prepareSortableOptions = () => {
      return {
        // 並び替え時のアニメーション時間
        animation: 150,
        // 並び替え元要素のclass名を指定
        ghostClass: 'sorted',
        // ドラッグ中要素のclass名を指定
        dragClass: 'dragging',
        // ドラッグが終了したときに発火する関数
        onEnd: (event) => {
          self.props.onSorted(event.oldIndex, event.newIndex)
        },
      }
    }
  </script>
</ui-list>
