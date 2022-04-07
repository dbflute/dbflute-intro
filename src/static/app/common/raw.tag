<raw>
  <!--
  HTML要素をそのまま表示したいときに利用するタグ
  ルートのDOMノードに外から渡されたコンテンツをhtmlとしてセットする
  -->
  <script>
    this.root.innerHTML = opts.content
    this.on('update', function() {
      // コンテンツが更新されるごとにhtmlに反映する
      this.root.innerHTML = opts.content
    })
  </script>
</raw>
