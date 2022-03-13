<alter-check-checked-files>
  <!-- AlterDDLを一覧表示する画面 (written at 2022/03/13)
  機能:
   o AlterDDLをシンタックスハイライトとともに表示することができる
  作りの特徴:
   o prism.jsを使ってSQLハイライトしたhtml要素が利用元から渡されてくる
  -->
  <div class="ui list">
    <div class="item" each="{ file in opts.checkedfiles }">
      <a onclick="{ clickFileName.bind(this, file) }">{ file.fileName }</a>
      <div show="{ file.show }" class="ui message message-area">
        <!-- SQLの改行やインデントが反映されるようにpreタグを利用
          <pre><code>...</code></pre> は一行で記述しないと余分な改行やインデントが入ってしまうので注意
          cssのwhite-spaceを利用するとSQLの先頭に記述したインデントが効かなくなるのでこうするしかなさそう...
          #thinking: <pre><code>...</code></pre>を含めて共通のコンポーネントにした方が良い by hakiba
        -->
        <pre><code><raw content="{ file.content }"></raw></code></pre>
      </div>
    </div>
  </div>

  <script>
    /**
     * ファイルの表示・非表示を切り替えます
     * @param file クリックされたファイルのオブジェクト (NotNull)
     */
    this.clickFileName = (file) => {
      file.show = !(file.show)
    }
  </script>
</alter-check-checked-files>
