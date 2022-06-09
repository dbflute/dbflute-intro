<ui-list>
  <!-- ドラッグ・アンド・ドロップも可能な並べ替えができるリスト (written at 2022/06/02)
   機能:
    o 何らかのリスト形式のデータを、画面にリスト形式で表示することができる
    o 初期化方法によっては、リスト表示されたものをドラッグ・アンド・ドロップすることで、順序を入れ替えることができる

   作りの特徴:
    o 親タグから、ドラッグ・アンド・ドロップが可能かどうかを切り替えられるようにしている
    o 親タグから、画面に表示したいデータを受け取る
    o 親タグから、ドラッグ・アンド・ドロップしたとき実行すべき処理を受け取る
    o sortablejs というライブラリを利用して、ドラッグ・アンド・ドロップの挙動を実現している
    o liタグの中身は、親タグで入れ子に記載する
    o 親タグに記載されているliタグの中身からは、itemという変数を介してリスト形式のデータの1要素にアクセスできるようにしている
    o ドラッグ・アンド・ドロップがあったとき、親タグで保持しているデータが書き換わり、再度データが親タグから渡される
    o 再度データが親タグから渡されたとき、このタグ全体が書き換えられる
   -->
  <ul ref="items" class="ui divided items segment">
    <!-- ここで item という変数で展開しているので、親タグの入れ子になっているHTMLタグの中では item という変数を参照している -->
    <li class="item" each="{ item in state.items }">
      <!-- 
        親タグで入れ子に書かれている HTML タグを展開する
        https://v3.riotjs.vercel.app/ja/api/#-yield%E3%81%AB%E3%82%88%E3%82%8Bhtml%E3%81%AE%E5%85%A5%E3%82%8C%E5%AD%90
      -->
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
      // 順序の変更が可能かどうか
      // sortable="{ true }" と指定した場合、opts.sortableに"sortable"という文字列が渡ってくるのでbooleanに変換しておく
      sortable: !!self.opts.sortable,

      // 順序の変更が起きたときに実行されるコールバック
      // 親タグで保持しているリスト形式のデータの入れ替えを行う処理が渡されることを想定している
      // (oldIndex : number, newIndex : number) => void
      onSorted: self.opts.onsorted,

      // 付加情報
      // 親タグから関数が渡され、親タグから実行されている
      options: self.opts.options
    }

    self.state = {
      // 表示するリスト
      items: self.opts.items
    }

    // =======================================================================================
    //                                                                               Lifecycle
    //                                                                               =========
    /**
     * マウント時の処理
     * ソート機能を提供する場合、sortablejsの初期化を行う
     */
    self.on('mount', () => {
      if (self.props.sortable) {
        // SortableJSによるソート機能を対象のDOM要素にbindする
        Sortable.create(self.refs.items, self.prepareSortableOptions())
      }
    })

    /**
     * update() 関数が呼び出されたときの処理
     * itemsを再度親タグで保持している値に書き換える
     */
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
