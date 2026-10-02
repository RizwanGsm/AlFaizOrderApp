package com.alfaiz.fastfood;
import android.app.*;import android.os.*;import android.content.*;import android.content.res.Configuration;import android.graphics.Color;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.text.*;import android.view.*;import android.view.animation.AnimationUtils;import android.widget.*;import java.net.URLEncoder;import java.net.URL;import java.net.HttpURLConnection;import java.util.*;
import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.net.Uri;import android.text.*;import android.view.*;import android.widget.*;import java.net.URLEncoder;import java.net.URL;import java.net.HttpURLConnection;import java.util.*;
public class MainActivity extends Activity{
 int ORANGE=Color.rgb(244,81,30),CREAM=Color.rgb(255,245,220),GREEN=Color.rgb(27,142,62),INK=Color.rgb(34,34,34); LinearLayout root,list; TextView total; EditText search; Map<String,Integer> cart=new LinkedHashMap<>(),prices=new HashMap<>(); SharedPreferences p; String cat="All"; ArrayList<Item> menu=new ArrayList<>();
 static class Item{String c,n;int p;Item(String c,String n,int p){this.c=c;this.n=n;this.p=p;}}
 public void onCreate(Bundle b){super.onCreate(b);p=getSharedPreferences("customer",0);seed();if(!p.getBoolean("done",false))signup();else main();}
 void seed(){add("Shawarma","Chicken Shawarma",160);add("Shawarma","Zinger Shawarma",230);add("Shawarma","Special Shawarma",230);add("Shawarma","Tika Shawarma",270);add("Shawarma","Malai Boti Shawarma",340);add("Shawarma","Chicken Achari Shawarma",190);add("Shawarma","Cheese Shawarma",230);add("Shawarma","Steak Shawarma",240);add("Pratha Rolls","Chicken Pratha Roll",220);add("Pratha Rolls","Tika Pratha Roll",290);add("Pratha Rolls","Malai Boti Pratha Roll",360);add("Pratha Rolls","Zinger Pratha Roll",280);add("Pratha Rolls","Kabab Pratha Roll",280);add("Burgers","Zinger Burger",350);add("Burgers","Peti Burger",250);add("Burgers","Boss Burger",500);add("Burgers","Tower Burger",660);add("Burgers","Anda Shami Burger",120);add("Burgers","Chicken Burger",230);add("Wings","BBQ Wings 6 pcs",300);add("Wings","BBQ Wings 12 pcs",600);add("Wings","Hot Wings 6 pcs",300);add("Wings","Hot Wings 12 pcs",600);add("Wings","Nuggets 6 pcs",300);add("Wings","Nuggets 12 pcs",600);String[] r={"Chicken Tika Pizza","Chicken Fajita Pizza","Chicken Supreme Pizza","Hot N Spicy Pizza","Cheese Lover Pizza","All Veggie Pizza"};for(String n:r){add("Pizza",n+" S",400);add("Pizza",n+" M",900);add("Pizza",n+" L",1250);add("Pizza",n+" XL",1750);}add("Fries","Regular Fries",250);add("Fries","Large Fries",400);add("Fries","Masala Fries",450);add("Fries","Special Loaded Fries",750);add("Fries","Crunchy Loaded Fries",850);add("Doner","Doner Half",350);add("Doner","Doner Full",650);add("Wraps","B.B.Q Wrap",400);add("Wraps","Arabic Wrap",400);add("Wraps","Tika Wrap",400);add("Wraps","Fajita Wrap",400);add("Wraps","Twister Wrap",450);add("Wraps","Grilled Wrap",450);add("Pasta","Special Pasta Half",450);add("Pasta","Special Pasta Full",800);add("Pasta","Creamy Pasta Half",450);add("Pasta","Creamy Pasta Full",800);add("Sandwich","Pizza Sandwich",600);add("Sandwich","Mexican Sandwich",650);add("Sandwich","Special Sandwich",600);add("Deals","Deal 1",1200);add("Deals","Deal 2",1500);add("Deals","Deal 3",1800);add("Deals","Deal 4",2800);add("Deals","Deal 5",3700);add("Deals","Deal 6",1250);add("Deals","Deal 7",1150);add("Deals","Deal 8",2350);add("Deals","Deal 9",1100);add("Deals","Deal 10",1100);}
 void add(String c,String n,int x){menu.add(new Item(c,n,x));prices.put(n,x);}
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
 base(); ScrollView sv=new ScrollView(this); LinearLayout page=new LinearLayout(this); page.setOrientation(LinearLayout.VERTICAL); page.setPadding(22,28,22,28);
 TextView icon=t("🍔",54,ORANGE); icon.setGravity(Gravity.CENTER); page.addView(icon,new LinearLayout.LayoutParams(-1,80));
 TextView brand=t("AL-FAIZ FAST FOOD",28,ORANGE); brand.setGravity(Gravity.CENTER); brand.setTypeface(null,Typeface.BOLD); page.addView(brand);
 TextView sub=t("Fresh food • Fast delivery • Easy ordering",15,Color.DKGRAY); sub.setGravity(Gravity.CENTER); page.addView(sub,new LinearLayout.LayoutParams(-1,48));
 LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(18,20,18,20); box.setBackground(cardBg(Color.WHITE,34)); box.setElevation(10);
 TextView w=t("Welcome! 👋",24,INK);w.setTypeface(null,Typeface.BOLD);box.addView(w,new LinearLayout.LayoutParams(-1,50));
 TextView info=t("Enter your details once and checkout will be much faster next time.",14,Color.DKGRAY);box.addView(info,new LinearLayout.LayoutParams(-1,50));
 EditText n=modernField("  👤  Full name"),ph=modernField("  📱  Phone number"),a=modernField("  📍  Delivery address");
 box.addView(n,new LinearLayout.LayoutParams(-1,58)); Space s1=new Space(this);box.addView(s1,new LinearLayout.LayoutParams(1,10));
 box.addView(ph,new LinearLayout.LayoutParams(-1,58)); Space s2=new Space(this);box.addView(s2,new LinearLayout.LayoutParams(1,10));
 box.addView(a,new LinearLayout.LayoutParams(-1,88)); Button go=pill("Continue to menu  →",ORANGE);box.addView(go,new LinearLayout.LayoutParams(-1,58));page.addView(box);
 TextView secure=t("🔒  Your delivery details are stored on this phone for your convenience.",12,Color.DKGRAY);secure.setGravity(Gravity.CENTER);page.addView(secure,new LinearLayout.LayoutParams(-1,60));
 sv.addView(page);root.addView(sv);
 go.setOnClickListener(v->{if(n.getText().toString().trim().isEmpty()||ph.getText().toString().trim().isEmpty()||a.getText().toString().trim().isEmpty()){Toast.makeText(this,"Please complete all fields",Toast.LENGTH_SHORT).show();return;}p.edit().putBoolean("done",true).putString("name",n.getText().toString().trim()).putString("phone",ph.getText().toString().trim()).putString("address",a.getText().toString().trim()).apply();main();});
}
 void main(){
 base();
 LinearLayout h=new LinearLayout(this);
 h.setGravity(Gravity.CENTER_VERTICAL);
 h.setPadding(16,0,8,0);
 h.setBackgroundColor(ORANGE);
 TextView title=t("AL-FAIZ FAST FOOD",21,Color.WHITE);
 title.setTypeface(null,Typeface.BOLD);
 h.addView(title,new LinearLayout.LayoutParams(0,dp(60),1));
 TextView prof=t("👤",22,Color.WHITE);
 prof.setGravity(Gravity.CENTER);
 prof.setBackground(cardBg(Color.rgb(220,65,20),30));
 h.addView(prof,new LinearLayout.LayoutParams(dp(46),dp(46)));
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
 String[] cats={"All","Shawarma","Pratha Rolls","Burgers","Wings","Pizza","Fries","Doner","Wraps","Pasta","Sandwich","Deals"};
 for(String c:cats){
  TextView q=menuText(c,13,c.equals(cat)?Color.WHITE:INK);
  q.setGravity(Gravity.CENTER);
  q.setTypeface(null,Typeface.BOLD);
  q.setBackground(cardBg(c.equals(cat)?ORANGE:Color.WHITE,24));
  q.setElevation(2);
  LinearLayout.LayoutParams qp=new LinearLayout.LayoutParams(
      c.length()>10?150:108,44);
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
  if(!i.n.toLowerCase().contains(q))continue;

  LinearLayout card=new LinearLayout(this);
  card.setOrientation(LinearLayout.HORIZONTAL);
  card.setGravity(Gravity.CENTER_VERTICAL);
  card.setPadding(10,10,10,10);
  card.setBackground(cardBg(Color.WHITE,22));
  card.setElevation(4);

  ImageView im=new ImageView(this);
  im.setScaleType(ImageView.ScaleType.CENTER_CROP);
  im.setBackground(cardBg(Color.rgb(245,235,225),18));
  im.setClipToOutline(true);
  loadFoodImage(im,i.c);
  card.addView(im,new LinearLayout.LayoutParams(dp(88),dp(88)));

  LinearLayout info=new LinearLayout(this);
  info.setOrientation(LinearLayout.VERTICAL);
  info.setGravity(Gravity.CENTER_VERTICAL);
  info.setPadding(12,0,0,0);

  TextView name=menuText(i.n,16,INK);
  name.setTypeface(null,Typeface.BOLD);
  name.setMaxLines(2);
  name.setEllipsize(TextUtils.TruncateAt.END);
  name.setGravity(Gravity.CENTER_VERTICAL);name.setIncludeFontPadding(false);name.setLineSpacing(0,1.0f);
  info.addView(name,new LinearLayout.LayoutParams(-1,dp(42)));

  LinearLayout bottom=new LinearLayout(this);
  bottom.setGravity(Gravity.CENTER_VERTICAL);

  TextView price=menuText("Rs "+i.p,17,ORANGE);
  price.setTypeface(null,Typeface.BOLD);
  price.setGravity(Gravity.CENTER_VERTICAL);price.setIncludeFontPadding(false);
  bottom.addView(price,new LinearLayout.LayoutParams(0,dp(42),1));

  Button add=pill("ADD",ORANGE);
  add.setTextSize(12);
  add.setPadding(8,0,8,0);
  bottom.addView(add,new LinearLayout.LayoutParams(dp(82),dp(40)));
  info.addView(bottom,new LinearLayout.LayoutParams(-1,dp(42)));

  card.addView(info,new LinearLayout.LayoutParams(0,dp(88),1));

  LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,dp(108));
  cp.setMargins(dp(3),dp(6),dp(3),dp(6));
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
 else url="https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=700&q=82";
 final String u=url;
 new AsyncTask<Void,Void,Bitmap>(){protected Bitmap doInBackground(Void...x){try{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection();c.setConnectTimeout(6000);c.setReadTimeout(8000);c.connect();Bitmap b=BitmapFactory.decodeStream(c.getInputStream());c.disconnect();return b;}catch(Exception e){return null;}}protected void onPostExecute(Bitmap b){if(b!=null){v.setImageBitmap(b);v.setAlpha(0f);v.animate().alpha(1f).setDuration(350).start();}}}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
}
 void sum(){int n=0,s=0;for(String k:cart.keySet()){n+=cart.get(k);s+=prices.get(k)*cart.get(k);}total.setText(n+" items  |  Rs "+s);}
 void cart(){base();root.addView(t("Your Order",27,ORANGE));ScrollView sv=new ScrollView(this);LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);int sum=0;for(String k:cart.keySet()){int q=cart.get(k),z=prices.get(k)*q;sum+=z;l.addView(t(q+" × "+k+" = Rs "+z,17,INK));}sv.addView(l);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));final int orderTotal=sum;root.addView(t("TOTAL: Rs "+orderTotal,22,ORANGE));Button wa=b("SEND ORDER TO WHATSAPP",GREEN);root.addView(wa,new LinearLayout.LayoutParams(-1,60));wa.setOnClickListener(v->send(orderTotal));Button back=b("BACK TO MENU",Color.DKGRAY);root.addView(back,new LinearLayout.LayoutParams(-1,55));back.setOnClickListener(v->main());}
 void send(int total){if(cart.isEmpty()){Toast.makeText(this,"Cart is empty",0).show();return;}StringBuilder m=new StringBuilder("*AL-FAIZ FAST FOOD ORDER*\\n\\n");m.append("Name: ").append(p.getString("name","")).append("\\n");m.append("Phone: ").append(p.getString("phone","")).append("\\n");m.append("Address: ").append(p.getString("address","")).append("\\n\\n");for(String k:cart.keySet())m.append(cart.get(k)).append(" x ").append(k).append(" — Rs ").append(prices.get(k)*cart.get(k)).append("\\n");m.append("\\n*TOTAL: Rs ").append(total).append("*");try{String u=URLEncoder.encode(m.toString(),"UTF-8");startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://wa.me/923177052500?text="+u)));}catch(Exception e){Toast.makeText(this,"WhatsApp unavailable",1).show();}}\n void profile(){base();root.addView(t("Customer Details",25,ORANGE));EditText n=f("Name");n.setText(p.getString("name",""));EditText ph=f("Phone");ph.setText(p.getString("phone",""));EditText a=f("Address");a.setText(p.getString("address",""));root.addView(n);root.addView(ph);root.addView(a);Button save=b("SAVE",ORANGE);root.addView(save);save.setOnClickListener(v->{p.edit().putString("name",n.getText().toString()).putString("phone",ph.getText().toString()).putString("address",a.getText().toString()).apply();main();});}

 @Override public void onConfigurationChanged(Configuration newConfig){super.onConfigurationChanged(newConfig);if(p!=null&&p.getBoolean("done",false)){String query=search==null?"":search.getText().toString();main();if(search!=null){search.setText(query);search.setSelection(search.length());}}}
}