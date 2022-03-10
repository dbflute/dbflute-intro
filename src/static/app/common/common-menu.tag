<common-menu>
  <!-- DBFlute Intro のクライアント選択画面のメニュー以外の部分 (written at 2022/03/10)
    機能:
      o DBFlute Intro 画面全体の左側のメニュー
        o のうち、クライアントが選択されて無いときに表示されるもの
      o DBFlute のドキュメントへのリンクになっている

      作りの特徴:
      o 特に logic は持っていない
  -->
  <!-- #thinking common ってほど common で使われいてる tag でもなくない！？ by cabos (2022/02/26) -->
  <div class="ui left fixed small inverted vertical menu">

    <!-- メニューの一番上に表示されている「DBFlute Intro」の文字列 -->
    <menu-home></menu-home>

    <!-- リンク --> 
    <a class="item" href="http://dbflute.seasar.org/ja/manual/function/generator/intro/" target="_blank">Intro's Document Page</a>
    <a class="item" href="http://github.com/dbflute/dbflute-intro" target="_blank">Intro's Github Page</a>
    <a class="item" href="http://dbflute.seasar.org/" target="_blank">DBFlute Top Page</a>
  </div>
</common-menu>
