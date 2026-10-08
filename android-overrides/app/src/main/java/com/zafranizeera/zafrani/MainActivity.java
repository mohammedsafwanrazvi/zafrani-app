package com.zafranizeera.zafrani;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.BridgeWebViewClient;
import java.util.HashMap;

public class MainActivity extends BridgeActivity {
  private static final HashMap<String, String> ROUTES = new HashMap<>();
  private static final String HOST = Uri.parse("https://zafranizeera.com").getHost();
  private long lastBack = 0;
  static {
    ROUTES.put("sms", "native");
    ROUTES.put("tel", "native");
    ROUTES.put("upi", "native");
    ROUTES.put("mailto", "native");
    ROUTES.put("external", "browser");
    ROUTES.put("whatsapp", "native");
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WebView web = bridge.getWebView();
    WebSettings ws = web.getSettings();
    ws.setJavaScriptEnabled(true);
    ws.setDomStorageEnabled(true);
    ws.setDatabaseEnabled(true);
    ws.setLoadsImagesAutomatically(true);
    ws.setBlockNetworkImage(false);
    ws.setBlockNetworkLoads(false);
    ws.setAllowFileAccess(true);
    ws.setAllowContentAccess(true);
    ws.setMediaPlaybackRequiresUserGesture(false);
    ws.setUseWideViewPort(true);
    ws.setLoadWithOverviewMode(true);
    ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    ws.setCacheMode(WebSettings.LOAD_DEFAULT);
    CookieManager.getInstance().setAcceptCookie(true);
    CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
    bridge.setWebViewClient(new BridgeWebViewClient(bridge) {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
        Uri u = req.getUrl();
        String s = u.toString(), scheme = u.getScheme() == null ? "" : u.getScheme();
        String type = null;
        if (scheme.equals("whatsapp") || s.contains("wa.me") || s.contains("api.whatsapp.com")) type = "whatsapp";
        else if (scheme.equals("upi")) type = "upi";
        else if (scheme.equals("tel")) type = "tel";
        else if (scheme.equals("mailto")) type = "mailto";
        else if (scheme.equals("sms")) type = "sms";
        else if (u.getHost() != null && HOST != null && !u.getHost().endsWith(HOST)) type = "external";
        if (type == null || "webview".equals(ROUTES.get(type))) return super.shouldOverrideUrlLoading(view, req);
        try { startActivity(new Intent(Intent.ACTION_VIEW, u)); } catch (Exception e) {
          Toast.makeText(MainActivity.this, "No app found to open this link", Toast.LENGTH_SHORT).show();
        }
        return true;
      }
    });
  }

  @Override
  public void onBackPressed() {
    WebView wv = bridge.getWebView();
    if (true && wv.canGoBack()) { wv.goBack(); return; }
    if (true) {
      if (System.currentTimeMillis() - lastBack < 2000) { finish(); return; }
      lastBack = System.currentTimeMillis();
      Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
      return;
    }
    super.onBackPressed();
  }
}
