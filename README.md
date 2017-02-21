
## luojiSDK使用方法

### 引入方法
 - 在gradle文件中输入
 ```
 compile 'com.luoji:pay:1.1'
  ```
   即可

- 调起webview 页面需要传入的参数

     ```
     Intent  intent = new Intent(this, LjMainActivity.class);
     intent.putExtra("appid","XXX");
     intent.putExtra("appkey","XXXX");
     intent.putExtra("userid","XXX");
     XXXActivity.this.startActivity(intent);
     ```

- js向原生发送消息

  ```
      function submit (){
            //必须是json格式 status=1是支付成功,其他状态H5自行处理，
            //比如支付失败的提示，比如关闭当前页面，可以调用下面方法
            Android.onSubmit("{data:{'msg':'支付成功',uid:'121212',status:1}}");
      }
    ```

- js关闭当前原生webview

    ```
    function closeWnd( ){
         	var close = window.confirm("需要关闭当前页面");
         	if(close){
         		Android.closeCurrentWindow();
         	}
         }
     ```

==========================================================================
##  用户接收返回参数

- 注册eventbus  `EventBus.getDefault().register(this);`
- EventBus用法可以参考github https://github.com/greenrobot/EventBus

-    event.msg为支付的返回结果，json由用户自己处理业务逻辑

    ```
     @Subscribe(threadMode = ThreadMode.BACKGROUND)
          public void onEventMainThread(BaseEvent event) {
               //全局任何地方都可以获取到支付结果，用户可以根据自己的业务逻辑自行处理
              Log.e("event", event.msg);

          }
    ```



