<alter-check-form>
  <!-- 新しいAlterDDLを作成するフォーム (written at 2022/03/13)
  機能:
    o 新しいファイルを作成するか既存のファイルを編集するか選択することができる
  -->
  <div class="ui segment">
    <div class="ui two column very relaxed stackable grid">
      <div class="column">
        <alter-check-begin-form ref="beginform" projectname="{ opts.projectname }" updatehandler="{ opts.updatehandler }" />
      </div>
      <div class="column">
        <alter-check-fix-form projectname="{ opts.projectname }" updatehandler="{ opts.updatehandler }" />
      </div>
    </div>
    <!-- semantic-uiを利用してフォームの間に区切り線を引いている -->
    <div class="ui vertical divider">
      Or
    </div>
  </div>
</alter-check-form>
