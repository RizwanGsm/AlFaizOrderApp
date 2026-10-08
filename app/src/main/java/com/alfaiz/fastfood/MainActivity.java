package com.alfaiz.fastfood;
import android.app.*;import android.os.*;import android.content.*;import android.content.res.Configuration;import android.graphics.Color;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.text.*;import android.view.*;import android.view.animation.AnimationUtils;import android.widget.*;import java.net.URLEncoder;import java.net.URL;import java.net.HttpURLConnection;import java.util.*;
import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.text.*;import android.view.*;import android.widget.*;import java.net.URLEncoder;import java.net.URL;import java.net.HttpURLConnection;import java.util.*;
public class MainActivity extends Activity{
 int ORANGE=Color.rgb(244,81,30),CREAM=Color.rgb(255,245,220),GREEN=Color.rgb(27,142,62),INK=Color.rgb(34,34,34); LinearLayout root,list; TextView total; EditText search; Map<String,Integer> cart=new LinkedHashMap<>(),prices=new HashMap<>(); SharedPreferences p; String screen=""; String cat="All"; ArrayList<Item> menu=new ArrayList<>(); String whatsapp="923168553075"; boolean appEnabled=true; static final String GITHUB_CONFIG_URL="https://raw.githubusercontent.com/RizwanGsm/AlFaizOrderApp/main/online/github-config.json";
 Handler remoteHandler=new Handler(Looper.getMainLooper()); Runnable remoteChecker;
 static class Item{String c,n,d;int p;Item(String c,String n,int p){this(c,n,p,"");}Item(String c,String n,int p,String d){this.c=c;this.n=n;this.p=p;this.d=d==null?"":d;}}
 public void onCreate(Bundle b){super.onCreate(b);p=getSharedPreferences("customer",0);seed();if(!GITHUB_CONFIG_URL.isEmpty()){loadOnline();startRemoteMonitoring();}else showInitial();}
@Override protected void onResume(){super.onResume();if(!GITHUB_CONFIG_URL.isEmpty()&&!appEnabled){return;}if(!GITHUB_CONFIG_URL.isEmpty())fetchRemote(false);}
 void showInitial(){if(!p.getBoolean("done",false))signup();else main();}
 void loadOnline(){fetchRemote(true);}
 void fetchRemote(final boolean initial){new AsyncTask<Void,Void,Boolean>(){String data;protected Boolean doInBackground(Void...x){try{HttpURLConnection c=(HttpURLConnection)new URL(GITHUB_CONFIG_URL+"?t="+System.currentTimeMillis()).openConnection();c.setConnectTimeout(7000);c.setReadTimeout(9000);c.setRequestProperty("Cache-Control","no-cache, no-store");c.setRequestProperty("Pragma","no-cache");c.connect();if(c.getResponseCode()!=200){c.disconnect();return false;}java.io.InputStream is=c.getInputStream();java.io.ByteArrayOutputStream out=new java.io.ByteArrayOutputStream();byte[] buf=new byte[4096];int n;while((n=is.read(buf))>0)out.write(buf,0,n);data=out.toString("UTF-8");c.disconnect();return true;}catch(Exception e){return false;}}protected void onPostExecute(Boolean ok){if(ok){boolean parsed=applyOnline(data);if(!parsed){if(initial)shutdownScreenWithMessage("Unable to verify application settings.");return;}if(!appEnabled){shutdownScreen();remoteHandler.removeCallbacks(remoteChecker);}else if(initial){showInitial();}else if(menuChanged&&"menu".equals(screen)){main();Toast.makeText(MainActivity.this,"Menu updated from admin panel",Toast.LENGTH_SHORT).show();}}else if(initial){shutdownScreenWithMessage("Internet connection is required to verify this app.");}}}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);}
 void startRemoteMonitoring(){remoteChecker=new Runnable(){public void run(){fetchRemote(false);remoteHandler.postDelayed(this,5000);}};remoteHandler.postDelayed(remoteChecker,5000);}
 boolean menuChanged=false; String menuFingerprint(){StringBuilder b=new StringBuilder();for(Item i:menu)b.append(i.c).append((char)31).append(i.n).append((char)31).append(i.p).append((char)31).append(i.d).append((char)30);return b.toString();}\n boolean applyOnline(String json){String before=menuFingerprint();try{org.json.JSONObject root=new org.json.JSONObject(json);org.json.JSONObject settings=root.optJSONObject("settings");if(settings!=null){whatsapp=settings.optString("whatsapp",whatsapp).replace("+","").replace(" ","");if(!settings.has("appEnabled"))return false;appEnabled=settings.getBoolean("appEnabled");}else{return false;}org.json.JSONArray a=root.optJSONArray("menu");org.json.JSONObject mo=root.optJSONObject("menu");if(a!=null&&a.length()>0){menu.clear();prices.clear();for(int j=0;j<a.length();j++){org.json.JSONObject o=a.getJSONObject(j);if(o.optBoolean("enabled",true)){add(o.optString("category","Other"),o.getString("name"),o.getInt("price"),o.optString("details",""));}}}else if(mo!=null&&mo.length()>0){menu.clear();prices.clear();java.util.Iterator<String> it=mo.keys();while(it.hasNext()){org.json.JSONObject o=mo.getJSONObject(it.next());if(o.optBoolean("enabled",true)){add(o.optString("category","Other"),o.getString("name"),o.getInt("price"),o.optString("details",""));}}}else{return false;}menuChanged=!before.equals(menuFingerprint());if(menuChanged){java.util.Iterator<String> cartItems=cart.keySet().iterator();while(cartItems.hasNext())if(!prices.containsKey(cartItems.next()))cartItems.remove();if(!menuHasCategory(cat))cat="All";}}catch(Exception ignored){return false;}return true;} boolean menuHasCategory(String category){if("All".equals(category))return true;for(Item i:menu)if(i.c.equals(category))return true;return false;}
 void shutdownScreen(){shutdownScreenWithMessage("This application has been deactivated by the administrator.");}
 void shutdownScreenWithMessage(String message){screen="shutdown";base(); LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setGravity(Gravity.CENTER);box.setPadding(dp(28),dp(28),dp(28),dp(28)); TextView icon=t("🔒",58,ORANGE);icon.setGravity(Gravity.CENTER);box.addView(icon,new LinearLayout.LayoutParams(-1,dp(80))); TextView title=t("APP DEACTIVATED",25,INK);title.setTypeface(null,Typeface.BOLD);title.setGravity(Gravity.CENTER);box.addView(title,new LinearLayout.LayoutParams(-1,dp(55))); TextView msg=t(message,16,Color.DKGRAY);msg.setGravity(Gravity.CENTER);msg.setLineSpacing(4,1.1f);box.addView(msg,new LinearLayout.LayoutParams(-1,dp(90))); TextView note=t("Please contact Al-Faiz Fast Food for assistance.",14,Color.DKGRAY);note.setGravity(Gravity.CENTER);box.addView(note,new LinearLayout.LayoutParams(-1,dp(55)));root.addView(box,new LinearLayout.LayoutParams(-1,-1));}
 void seed(){add("Shawarma","Chicken Shawarma",160);add("Shawarma","Zinger Shawarma",230);add("Shawarma","Special Shawarma",230);add("Shawarma","Tika Shawarma",270);add("Shawarma","Malai Boti Shawarma",340);add("Shawarma","Chicken Achari Shawarma",190);add("Shawarma","Cheese Shawarma",230);add("Shawarma","Steak Shawarma",240);add("Pratha Rolls","Chicken Pratha Roll",220);add("Pratha Rolls","Tika Pratha Roll",290);add("Pratha Rolls","Malai Boti Pratha Roll",360);add("Pratha Rolls","Zinger Pratha Roll",280);add("Pratha Rolls","Kabab Pratha Roll",280);add("Burgers","Zinger Burger",350);add("Burgers","Peti Burger",250);add("Burgers","Boss Burger",500);add("Burgers","Tower Burger",660);add("Burgers","Anda Shami Burger",120);add("Burgers","Chicken Burger",230);add("Wings","BBQ Wings 6 pcs",300);add("Wings","BBQ Wings 12 pcs",600);add("Wings","Hot Wings 6 pcs",300);add("Wings","Hot Wings 12 pcs",600);add("Wings","Nuggets 6 pcs",300);add("Wings","Nuggets 12 pcs",600);String[] r={"Chicken Tika Pizza","Chicken Fajita Pizza","Chicken Supreme Pizza","Hot N Spicy Pizza","Cheese Lover Pizza","All Veggie Pizza"};for(String n:r){add("Pizza",n+" S",400);add("Pizza",n+" M",900);add("Pizza",n+" L",1250);add("Pizza",n+" XL",1750);}add("Fries","Regular Fries",250);add("Fries","Large Fries",400);add("Fries","Masala Fries",450);add("Fries","Special Loaded Fries",750);add("Fries","Crunchy Loaded Fries",850);add("Doner","Doner Half",350);add("Doner","Doner Full",650);add("Wraps","B.B.Q Wrap",400);add("Wraps","Arabic Wrap",400);add("Wraps","Tika Wrap",400);add("Wraps","Fajita Wrap",400);add("Wraps","Twister Wrap",450);add("Wraps","Grilled Wrap",450);add("Pasta","Special Pasta Half",450);add("Pasta","Special Pasta Full",800);add("Pasta","Creamy Pasta Half",450);add("Pasta","Creamy Pasta Full",800);add("Sandwich","Pizza Sandwich",600);add("Sandwich","Mexican Sandwich",650);add("Sandwich","Special Sandwich",600);add("Deals","Deal 1",1200,"Includes: 3 Small Pizzas • 1 Liter Bottle");add("Deals","Deal 2",1500,"Includes: 1 Medium Pizza • 6 Nuggets • ½ Liter Bottle");add("Deals","Deal 3",1800,"Includes: 2 Medium Pizzas • 1 Liter Bottle");add("Deals","Deal 4",2800,"Includes: 2 Large Pizzas • 1½ Liter Bottle");add("Deals","Deal 5",3700,"Includes: 1 Large Pizza • 1 XL Pizza • 2 Liter Bottles");add("Deals","Deal 6",1250,"Includes: 1 Regular Fries • 6 Hot Wings • 2 Zinger Burgers • 1 Liter Bottle");add("Deals","Deal 7",1150,"Includes: 1 Small Pizza • 1 Pasta • 6 Hot Wings • 1 Liter Bottle");add("Deals","Deal 8",2350,"Includes: 1 Large Special Pizza • 2 Zinger Burgers • 2 Chicken Shawarma • 1 Liter Bottle");add("Deals","Deal 9",1100,"Includes: 4 Anda Shami Burgers • 4 Chicken Shawarma • 1 Liter Bottle");add("Deals","Deal 10",1100,"Includes: 2 Shawarma • 2 Chicken Shawarma • 6 Nuggets • 1 Liter Bottle");add("Deals","Deal 11",2090,"Includes: 7 Chicken Shawarma • 1 Medium Pizza • 1½ Liter Bottle");add("Deals","Deal 12",1500,"Includes: 1 Full Pasta • 1 Regular Fries • 1 Spring Roll • ½ Liter Bottle");add("Deals","Deal 13",750,"Includes: 1 Chicken Roll • 6 Hot Wings • ½ Liter Bottle");add("Deals","Deal 14",900,"Includes: 1 Small Pizza • 1 Full Pasta • ½ Liter Bottle");add("Deals","Deal 15",950,"Includes: 2 Small Pizzas • 1 Liter Bottle");add("Deals","Deal 16",1400,"Includes: 1 Medium Pizza • 1 Spring Roll • 1 Liter Bottle");add("Deals","Deal 17",1350,"Includes: 1 BBQ Roll • 1 Malai Boti Roll • ½ Pasta • 1 Liter Bottle");add("Deals","Deal 18",600,"Includes: 1 Zinger Burger • 1 Peti Burger • ½ Liter Bottle");add("Deals","Deal 19",1350,"Includes: 1 Small Pizza • 1 Special Roll • 1 Small Pasta • 1 Liter Bottle");add("Deals","Deal 20",900,"Includes: 2 Full Pasta • ½ Liter Bottle");}
 void add(String c,String n,int x){add(c,n,x,"");} void add(String c,String n,int x,String d){menu.add(new Item(c,n,x,d));prices.put(n,x);}
 TextView t(String s,int z,int col){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(col);v.setPadding(16,8,16,8);return v;}
TextView menuText(String s,float z,int col){TextView v=new TextView(this);v.setText(s);v.setTextSize(z);v.setTextColor(col);v.setPadding(0,0,0,0);return v;}
 int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+0.5f);}
GradientDrawable cardBg(int c,float r){GradientDrawable g=new GradientDrawable();g.setColor(c);g.setCornerRadius(r);return g;}
Button pill(String s,int c){Button b=new Button(this);b.setText(s);b.setTextColor(Color.WHITE);b.setTextSize(13);b.setAllCaps(false);b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);b.setBackground(cardBg(c,28));b.setStateListAnimator(null);return b;}
EditText modernField(String hint){EditText e=f(hint);e.setBackground(cardBg(Color.WHITE,28));e.setElevation(3);e.setPadding(18,0,18,0);return e;}
 Button b(String s,int col){Button x=new Button(this);x.setText(s);x.setTextColor(Color.WHITE);x.setTextSize(14);x.setText(s);x.setBackgroundColor(col);return x;}
 EditText f(String h){EditText e=new EditText(this);e.setHint(h);e.setTextSize(16);e.setSingleLine();e.setPadding(18,0,18,0);return e;}
 void base(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(CREAM);setContentView(root);}
 void signup(){
 screen="signup";
 base(); ScrollView sv=new ScrollView(this); LinearLayout page=new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL); page.setPadding(dp(18),dp(22),dp(18),dp(24));
 TextView icon=t("🍔",54,ORANGE); icon.setGravity(Gravity.CENTER); page.addView(icon,new LinearLayout.LayoutParams(-1,80));
 TextView brand=t("AL-FAIZ FAST FOOD",28,ORANGE); brand.setGravity(Gravity.CENTER); brand.setTypeface(null,Typeface.BOLD); page.addView(brand);
 TextView sub=t("Fresh food • Fast delivery • Easy ordering",15,Color.DKGRAY); sub.setGravity(Gravity.CENTER); page.addView(sub,new LinearLayout.LayoutParams(-1,dp(48)));
 LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(dp(18),dp(22),dp(18),dp(22)); box.setBackground(cardBg(Color.WHITE,34)); box.setElevation(10);
 TextView w=t("Welcome! 👋",24,INK);w.setTypeface(null,Typeface.BOLD);box.addView(w,new LinearLayout.LayoutParams(-1,dp(52)));
 TextView info=t("Enter your details once and checkout will be much faster next time.",14,Color.DKGRAY);info.setLineSpacing(2,1.05f); box.addView(info,new LinearLayout.LayoutParams(-1,dp(58)));
 EditText n=modernField("  👤  Full name"),ph=modernField("  📱  Phone number"),a=modernField("  📍  Delivery address"); n.setInputType(0x0000c001); ph.setInputType(2); a.setSingleLine(false); a.setGravity(Gravity.TOP|Gravity.LEFT); a.setPadding(dp(18),dp(14),dp(18),dp(14));
 box.addView(n,new LinearLayout.LayoutParams(-1,dp(60))); Space s1=new Space(this);box.addView(s1,new LinearLayout.LayoutParams(1,dp(14)));
 box.addView(ph,new LinearLayout.LayoutParams(-1,dp(60))); Space s2=new Space(this);box.addView(s2,new LinearLayout.LayoutParams(1,dp(14)));
 box.addView(a,new LinearLayout.LayoutParams(-1,dp(104))); Space s3=new Space(this);box.addView(s3,new LinearLayout.LayoutParams(1,dp(16))); Button go=pill("Continue to menu  →",ORANGE);box.addView(go,new LinearLayout.LayoutParams(-1,dp(56)));LinearLayout.LayoutParams boxLp=new LinearLayout.LayoutParams(-1,-2); boxLp.setMargins(0,dp(8),0,dp(8)); page.addView(box,boxLp);
 TextView secure=t("🔒  Your delivery details are stored on this phone for your convenience.",12,Color.DKGRAY);secure.setGravity(Gravity.CENTER);page.addView(secure,new LinearLayout.LayoutParams(-1,dp(64)));
 sv.addView(page);root.addView(sv);
 go.setOnClickListener(v->{if(n.getText().toString().trim().isEmpty()||ph.getText().toString().trim().isEmpty()||a.getText().toString().trim().isEmpty()){Toast.makeText(this,"Please complete all fields",Toast.LENGTH_SHORT).show();return;}p.edit().putBoolean("done",true).putString("name",n.getText().toString().trim()).putString("phone",ph.getText().toString().trim()).putString("address",a.getText().toString().trim()).apply();main();});
}
 void main(){
 screen="menu";
 base();
 LinearLayout h=new LinearLayout(this);
 h.setGravity(Gravity.CENTER_VERTICAL);
 h.setPadding(16,0,8,0);
 h.setBackgroundColor(ORANGE);
 TextView title=t("AL-FAIZ FAST FOOD",21,Color.WHITE);
 title.setTypeface(null,Typeface.BOLD);
 h.addView(title,new LinearLayout.LayoutParams(0,dp(60),1));
 TextView refresh=t("↻",25,Color.WHITE);
 refresh.setGravity(Gravity.CENTER);
 refresh.setBackground(cardBg(Color.rgb(220,65,20),30));
 h.addView(refresh,new LinearLayout.LayoutParams(dp(46),dp(46)));
 refresh.setOnClickListener(v->{
  refresh.setEnabled(false);
  Toast.makeText(this,"Refreshing menu...",Toast.LENGTH_SHORT).show();
  fetchRemote(false);
  new Handler(Looper.getMainLooper()).postDelayed(()->refresh.setEnabled(true),1800);
 });
 TextView prof=t("👤",22,Color.WHITE);
 prof.setGravity(Gravity.CENTER);
 prof.setBackground(cardBg(Color.rgb(220,65,20),30));
 LinearLayout.LayoutParams plp=new LinearLayout.LayoutParams(dp(46),dp(46));
 plp.setMargins(dp(6),0,0,0);
 h.addView(prof,plp);
 prof.setOnClickListener(v->profile());
 root.addView(h,new LinearLayout.LayoutParams(-1,dp(60)));

 search=f("Search menu...");
 search.setTextSize(16);
 search.setSingleLine(true);
 search.setBackground(cardBg(Color.WHITE,22));
 search.setElevation(2);
 search.setPadding(18,0,18,0);
 LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,dp(52));
 sp.setMargins(dp(10),dp(8),dp(10),dp(6));
 root.addView(search,sp);
 search.addTextChangedListener(new TextWatcher(){
  public void beforeTextChanged(CharSequence s,int a,int c,int d){}
  public void onTextChanged(CharSequence s,int a,int b,int c){render();}
  public void afterTextChanged(Editable e){}
 });

 HorizontalScrollView hs=new HorizontalScrollView(this);
 hs.setHorizontalScrollBarEnabled(false);
 LinearLayout cs=new LinearLayout(this);
 cs.setGravity(Gravity.CENTER_VERTICAL);
 cs.setPadding(10,4,10,4);
 ArrayList<String> cats=new ArrayList<>();cats.add("All");for(Item item:menu)if(!cats.contains(item.c))cats.add(item.c);
 for(String c:cats){
  TextView q=menuText(c,13,c.equals(cat)?Color.WHITE:INK);
  q.setGravity(Gravity.CENTER);
  q.setTypeface(null,Typeface.BOLD);
  q.setBackground(cardBg(c.equals(cat)?ORANGE:Color.WHITE,24));
  q.setElevation(2);
  LinearLayout.LayoutParams qp=new LinearLayout.LayoutParams(
      dp(c.length()>10?150:108),dp(44));
  qp.setMargins(dp(4),0,dp(4),0);
  cs.addView(q,qp);
  q.setOnClickListener(v->{cat=c;main();});
 }
 hs.addView(cs);
 root.addView(hs,new LinearLayout.LayoutParams(-1,dp(52)));

 ScrollView sv=new ScrollView(this);
 sv.setClipToPadding(false);
 list=new LinearLayout(this);
 list.setOrientation(LinearLayout.VERTICAL);
 list.setPadding(dp(10),dp(6),dp(10),dp(14));
 sv.addView(list,new ScrollView.LayoutParams(-1,-2));
 root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));

 LinearLayout bar=new LinearLayout(this);
 bar.setGravity(Gravity.CENTER_VERTICAL);
 bar.setPadding(dp(12),dp(6),dp(10),dp(6));
 bar.setBackgroundColor(Color.WHITE);
 bar.setElevation(12);
 total=t("0 items  |  Rs 0",16,INK);
 total.setTypeface(null,Typeface.BOLD);
 bar.addView(total,new LinearLayout.LayoutParams(0,dp(56),1));
 Button c=pill("VIEW CART",GREEN);
 bar.addView(c,new LinearLayout.LayoutParams(dp(135),dp(48)));
 c.setOnClickListener(v->cart());
 root.addView(bar,new LinearLayout.LayoutParams(-1,dp(68)));
 render();
}
 void render(){
 list.removeAllViews();
 String q=search==null?"":search.getText().toString().trim().toLowerCase();

 for(Item i:menu){
  if(!cat.equals("All")&&!i.c.equals(cat))continue;
  if(!i.n.toLowerCase().contains(q)&&!(i.c.equals("Deals")&&i.d.toLowerCase().contains(q)))continue;

  boolean deal=i.c.equals("Deals");
  String[] products=deal?dealProducts(i.d):new String[0];

  LinearLayout card=new LinearLayout(this);
  card.setOrientation(LinearLayout.VERTICAL);
  card.setPadding(dp(10),dp(10),dp(10),dp(10)); card.setClipChildren(false);
  card.setBackground(cardBg(Color.WHITE,24));
  card.setElevation(5);

  LinearLayout top=new LinearLayout(this);
  top.setOrientation(LinearLayout.HORIZONTAL);
  top.setGravity(Gravity.CENTER_VERTICAL);

  ImageView im=new ImageView(this);
  im.setScaleType(ImageView.ScaleType.CENTER_CROP);
  im.setBackground(cardBg(Color.rgb(245,235,225),18));
  im.setClipToOutline(true);
  loadFoodImage(im,deal&&products.length>0?dealImageCategory(products[0]):i.c);
  top.addView(im,new LinearLayout.LayoutParams(dp(82),dp(82)));

  LinearLayout info=new LinearLayout(this);
  info.setOrientation(LinearLayout.VERTICAL);
  info.setGravity(Gravity.CENTER_VERTICAL);
  info.setPadding(dp(12),0,0,0);

  TextView name=menuText(i.n,18,INK);
  name.setTypeface(null,Typeface.BOLD);
  name.setMaxLines(2);
  name.setEllipsize(TextUtils.TruncateAt.END);
  name.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
  info.addView(name,new LinearLayout.LayoutParams(-1,dp(42)));

  if(!deal){
    TextView price=menuText("Rs "+i.p,17,ORANGE);
    price.setTypeface(null,Typeface.BOLD);
    info.addView(price,new LinearLayout.LayoutParams(-1,dp(30)));
  }

  top.addView(info,new LinearLayout.LayoutParams(0,dp(82),1));

  if(deal){
    LinearLayout priceBox=new LinearLayout(this);
    priceBox.setOrientation(LinearLayout.VERTICAL);
    priceBox.setGravity(Gravity.CENTER);
    TextView price=menuText("Rs "+i.p,17,ORANGE);
    price.setTypeface(null,Typeface.BOLD); price.setGravity(Gravity.CENTER);
    priceBox.addView(price,new LinearLayout.LayoutParams(dp(76),dp(34)));
    TextView tag=menuText("DEAL",10,GREEN); tag.setGravity(Gravity.CENTER);
    tag.setTypeface(null,Typeface.BOLD);
    priceBox.addView(tag,new LinearLayout.LayoutParams(dp(76),dp(24)));
    top.addView(priceBox,new LinearLayout.LayoutParams(dp(82),dp(82)));
  }

  card.addView(top,new LinearLayout.LayoutParams(-1,dp(82)));

  if(deal&&products.length>0){
    TextView included=menuText("INCLUDED IN THIS DEAL",12,ORANGE);
    included.setTypeface(null,Typeface.BOLD);
    included.setPadding(0,dp(10),0,dp(4));
    card.addView(included,new LinearLayout.LayoutParams(-1,dp(28)));

    for(String product:products){
      LinearLayout pr=new LinearLayout(this);
      pr.setGravity(Gravity.CENTER_VERTICAL);
      pr.setPadding(dp(4),dp(3),dp(4),dp(3));
      pr.setBackground(cardBg(Color.rgb(255,248,238),16));

      ImageView pim=new ImageView(this);
      pim.setScaleType(ImageView.ScaleType.CENTER_CROP);
      pim.setBackground(cardBg(Color.rgb(245,235,225),14));
      pim.setClipToOutline(true);
      loadFoodImage(pim,dealImageCategory(product));
      pr.addView(pim,new LinearLayout.LayoutParams(dp(46),dp(46)));

      TextView pt=menuText(product,13,INK);
      pt.setTypeface(null,Typeface.BOLD);
      pt.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
      pt.setMaxLines(2);
      pt.setEllipsize(TextUtils.TruncateAt.END);
      LinearLayout.LayoutParams ptp=new LinearLayout.LayoutParams(0,dp(50),1);
      ptp.setMargins(dp(9),0,0,0);
      pr.addView(pt,ptp);

      TextView dot=menuText("✓",16,GREEN);
      dot.setGravity(Gravity.CENTER);
      dot.setTypeface(null,Typeface.BOLD);
      pr.addView(dot,new LinearLayout.LayoutParams(dp(28),dp(50)));

      LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,dp(52));
      pp.setMargins(0,dp(3),0,dp(3));
      card.addView(pr,pp);
    }
  }else if(deal){
    TextView desc=menuText(i.d,12,Color.DKGRAY);
    desc.setMaxLines(2); desc.setEllipsize(TextUtils.TruncateAt.END);
    desc.setPadding(0,dp(8),0,dp(4));
    card.addView(desc,new LinearLayout.LayoutParams(-1,dp(50)));
  }

  LinearLayout bottom=new LinearLayout(this);
  bottom.setGravity(Gravity.CENTER_VERTICAL);
  if(!deal){
    TextView price=menuText("Rs "+i.p,17,ORANGE);
    price.setTypeface(null,Typeface.BOLD);
    bottom.addView(price,new LinearLayout.LayoutParams(0,dp(44),1));
  }else{
    TextView hint=menuText(products.length+" products included",12,Color.DKGRAY);
    hint.setTypeface(null,Typeface.BOLD);
    bottom.addView(hint,new LinearLayout.LayoutParams(0,dp(44),1));
  }
  Button add=pill(deal?"ADD DEAL":"ADD",ORANGE);
  add.setTextSize(12);
  bottom.addView(add,new LinearLayout.LayoutParams(dp(deal?108:82),dp(42)));
  card.addView(bottom,new LinearLayout.LayoutParams(-1,dp(48)));

  int cardHeight=deal?(190+(products.length*58)):160;
  LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(cardHeight));
  cp.setMargins(dp(3),dp(7),dp(3),dp(7));
  list.addView(card,cp);

  add.setOnClickListener(v->{
   cart.put(i.n,cart.getOrDefault(i.n,0)+1);
   sum();
   v.animate().scaleX(1.06f).scaleY(1.06f).setDuration(70).withEndAction(
     ()->v.animate().scaleX(1f).scaleY(1f).setDuration(70).start()
   ).start();
  });
 }
 sum();
}

String[] dealProducts(String d){
 if(d==null||d.trim().isEmpty())return new String[0];
 String x=d.replaceFirst("(?i)^Includes:\\s*","");
 String[] raw=x.split("\\s*•\\s*");
 ArrayList<String> out=new ArrayList<>();
 for(String z:raw)if(!z.trim().isEmpty())out.add(z.trim());
 return out.toArray(new String[0]);
}

String dealImageCategory(String product){
 String x=product.toLowerCase(Locale.US);
 if(x.contains("pizza"))return "Pizza";
 if(x.contains("shawarma")||x.contains("doner"))return "Shawarma";
 if(x.contains("burger"))return "Burgers";
 if(x.contains("fries"))return "Fries";
 if(x.contains("wing")||x.contains("nugget"))return "Wings";
 if(x.contains("pasta"))return "Pasta";
 if(x.contains("wrap")||x.contains("roll"))return "Wraps";
 if(x.contains("sandwich"))return "Sandwich";
 if(x.contains("bottle")||x.contains("drink")||x.contains("coke"))return "Drinks";
 return "Other";
}

void loadFoodImage(ImageView v,String category){
 String url=null;
 if(category.equals("Burgers"))url="https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Pizza"))url="https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Fries"))url="https://images.unsplash.com/photo-1573080496219-bb080dd4f877?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Wings"))url="https://images.unsplash.com/photo-1527477396000-e27163b481c2?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Pasta"))url="https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Sandwich"))url="https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Wraps"))url="https://images.unsplash.com/photo-1626700051175-6818013e1d4f?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Shawarma")||category.equals("Doner"))url="https://images.unsplash.com/photo-1529006557810-274b9b2fc783?auto=format&fit=crop&w=700&q=82";
 else if(category.equals("Drinks"))url="https://images.unsplash.com/photo-1629203849820-fdd70d49c38e?auto=format&fit=crop&w=700&q=82";
 else url="https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=700&q=82";
 final String u=url;
 new AsyncTask<Void,Void,Bitmap>(){protected Bitmap doInBackground(Void...x){try{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection();c.setConnectTimeout(6000);c.setReadTimeout(8000);c.connect();Bitmap b=BitmapFactory.decodeStream(c.getInputStream());c.disconnect();return b;}catch(Exception e){return null;}}protected void onPostExecute(Bitmap b){if(b!=null){v.setImageBitmap(b);v.setAlpha(0f);v.animate().alpha(1f).setDuration(350).start();}}}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
}
 void sum(){int n=0,s=0;for(String k:cart.keySet()){n+=cart.get(k);s+=prices.get(k)*cart.get(k);}total.setText(n+" items  |  Rs "+s);}
 void cart(){
   screen="cart";
   base();
   LinearLayout head=new LinearLayout(this);
   head.setGravity(Gravity.CENTER_VERTICAL);
   head.setPadding(dp(14),dp(8),dp(8),dp(8));
   TextView back=t("‹",38,ORANGE); back.setGravity(Gravity.CENTER);
   head.addView(back,new LinearLayout.LayoutParams(dp(52),dp(58)));
   TextView title=t("Your Cart",25,INK); title.setTypeface(null,Typeface.BOLD);
   head.addView(title,new LinearLayout.LayoutParams(0,dp(58),1));
   TextView count=t(cart.size()+" items",14,Color.DKGRAY); count.setGravity(Gravity.CENTER);
   head.addView(count,new LinearLayout.LayoutParams(dp(80),dp(58)));
   root.addView(head);
   back.setOnClickListener(v->main());

   ScrollView sv=new ScrollView(this);
   LinearLayout l=new LinearLayout(this);
   l.setOrientation(LinearLayout.VERTICAL);
   l.setPadding(dp(10),dp(4),dp(10),dp(12));

   if(cart.isEmpty()){
     TextView empty=t("🛒\n\nYour cart is empty\nAdd something delicious from the menu.",18,Color.DKGRAY);
     empty.setGravity(Gravity.CENTER);
     l.addView(empty,new LinearLayout.LayoutParams(-1,dp(260)));
     Button shop=pill("BROWSE MENU",ORANGE);
     l.addView(shop,new LinearLayout.LayoutParams(-1,dp(52)));
     shop.setOnClickListener(v->main());
   }else{
     for(String k:new ArrayList<>(cart.keySet())){
       final String item=k;
       int q=cart.get(k);
       int z=prices.get(k)*q;
       LinearLayout row=new LinearLayout(this);
       row.setGravity(Gravity.CENTER_VERTICAL);
       row.setPadding(dp(10),dp(8),dp(8),dp(8));
       row.setBackground(cardBg(Color.WHITE,20));
       row.setElevation(3);
       LinearLayout info=new LinearLayout(this);
       info.setOrientation(LinearLayout.VERTICAL);
       info.setGravity(Gravity.CENTER_VERTICAL);
       info.setVisibility(View.VISIBLE);
       TextView nm=menuText(item,16,INK);
       nm.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
       nm.setMaxLines(2);
       nm.setEllipsize(TextUtils.TruncateAt.END);
       nm.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
       nm.setIncludeFontPadding(false);
       TextView pr=menuText("Rs "+prices.get(k)+" each",13,Color.DKGRAY);
       pr.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
       pr.setIncludeFontPadding(false);
       info.addView(nm,new LinearLayout.LayoutParams(-1,dp(44)));
       info.addView(pr,new LinearLayout.LayoutParams(-1,dp(30)));
       LinearLayout.LayoutParams infoLp=new LinearLayout.LayoutParams(0,dp(82),1f);
       infoLp.setMargins(0,0,dp(8),0);
       row.addView(info,infoLp);
       LinearLayout qty=new LinearLayout(this); qty.setGravity(Gravity.CENTER);
       Button minus=pill("−",Color.DKGRAY); Button plus=pill("+",ORANGE);
       TextView qq=menuText(String.valueOf(q),17,INK); qq.setGravity(Gravity.CENTER); qq.setTypeface(null,Typeface.BOLD);
       qty.addView(minus,new LinearLayout.LayoutParams(dp(42),dp(42)));
       qty.addView(qq,new LinearLayout.LayoutParams(dp(36),dp(42)));
       qty.addView(plus,new LinearLayout.LayoutParams(dp(42),dp(42)));
       row.addView(qty);
       TextView lineTotal=menuText("Rs "+z,16,ORANGE); lineTotal.setGravity(Gravity.CENTER); lineTotal.setTypeface(null,Typeface.BOLD);
       row.addView(lineTotal,new LinearLayout.LayoutParams(dp(78),dp(82)));
       LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(-1,dp(98)); rp.setMargins(0,dp(5),0,dp(5));
       l.addView(row,rp);
       minus.setOnClickListener(v->{int nq=cart.getOrDefault(item,0)-1;if(nq<=0)cart.remove(item);else cart.put(item,nq);cart();});
       plus.setOnClickListener(v->{cart.put(item,cart.getOrDefault(item,0)+1);cart();});
     }
     Space gap=new Space(this);l.addView(gap,new LinearLayout.LayoutParams(1,dp(8)));
     Button clear=pill("CLEAR CART",Color.DKGRAY);l.addView(clear,new LinearLayout.LayoutParams(-1,dp(48)));
     clear.setOnClickListener(v->{cart.clear();cart();});
     TextView thanks=t("Thank you for choosing Al-Faiz Fast Food ❤️\\nThanks to Rizwan Ali",14,Color.DKGRAY);
     thanks.setGravity(Gravity.CENTER);
     thanks.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
     thanks.setPadding(0,dp(14),0,dp(8));
     l.addView(thanks,new LinearLayout.LayoutParams(-1,dp(68)));
   }
   sv.addView(l);
   root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));

   int subtotal=0;for(String k:cart.keySet())subtotal+=prices.get(k)*cart.get(k);
   int orderTotal=subtotal;
   LinearLayout summary=new LinearLayout(this);summary.setOrientation(LinearLayout.VERTICAL);summary.setPadding(dp(14),dp(8),dp(14),dp(8));summary.setBackgroundColor(Color.WHITE);summary.setElevation(12);
   TextView sub=menuText("Subtotal: Rs "+subtotal,15,INK);summary.addView(sub,new LinearLayout.LayoutParams(-1,dp(28)));
   TextView ttl=menuText("TOTAL: Rs "+orderTotal,21,ORANGE);ttl.setTypeface(null,Typeface.BOLD);summary.addView(ttl,new LinearLayout.LayoutParams(-1,dp(38)));
   Button wa=pill("PLACE ORDER ON WHATSAPP",GREEN);summary.addView(wa,new LinearLayout.LayoutParams(-1,dp(50)));
   wa.setEnabled(!cart.isEmpty());wa.setAlpha(cart.isEmpty()?0.5f:1f);wa.setOnClickListener(v->send(orderTotal));
   root.addView(summary);
 }
  void send(int total){
    if(cart.isEmpty()){ Toast.makeText(this,"Cart is empty",0).show(); return; }
    StringBuilder m=new StringBuilder("*AL-FAIZ FAST FOOD ORDER*");
    m.append((char)10).append((char)10);
    m.append("Name: ").append(p.getString("name","")).append((char)10);
    m.append("Phone: ").append(p.getString("phone","")).append((char)10);
    m.append("Address: ").append(p.getString("address","")).append((char)10).append((char)10);
    for(String k:cart.keySet()){ m.append(cart.get(k)).append(" x ").append(k).append(" — Rs ").append(prices.get(k)*cart.get(k)).append((char)10); for(Item it:menu){if(it.n.equals(k)&&!it.d.isEmpty()){m.append("   ").append(it.d).append((char)10);break;}} }
    m.append((char)10).append("*TOTAL: Rs ").append(total).append("*");
    try{
      String u=URLEncoder.encode(m.toString(),"UTF-8");
      startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/"+whatsapp+"?text="+u)));
    }catch(Exception e){ Toast.makeText(this,"WhatsApp unavailable",1).show(); }
  }
  void profile(){
    screen="profile";
    base(); root.addView(t("Customer Details",25,ORANGE));
    EditText n=f("Name"); n.setText(p.getString("name",""));
    EditText ph=f("Phone"); ph.setText(p.getString("phone",""));
    EditText a=f("Address"); a.setText(p.getString("address",""));
    root.addView(n); root.addView(ph); root.addView(a);
    Button save=b("SAVE",ORANGE); root.addView(save);
    save.setOnClickListener(v->{p.edit().putString("name",n.getText().toString()).putString("phone",ph.getText().toString()).putString("address",a.getText().toString()).apply(); main();});
  }
  @Override protected void onDestroy(){remoteHandler.removeCallbacksAndMessages(null);super.onDestroy();}
  @Override public void onBackPressed(){
    if("cart".equals(screen)||"profile".equals(screen)){ main(); return; }
    if("menu".equals(screen)){ super.onBackPressed(); return; }
    if("signup".equals(screen)){ super.onBackPressed(); return; }
    super.onBackPressed();
  }

}