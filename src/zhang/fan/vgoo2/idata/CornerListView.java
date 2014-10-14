package zhang.fan.vgoo2.idata;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Ô²½ÇListView
 */
public class CornerListView extends ListView {
    public CornerListView(Context context) {
        super(context);
    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//	@Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//        		System.out.println("MotionEvent.ACTION_DOWN===");
//                int x = (int) ev.getX();
//                int y = (int) ev.getY();
//                int itemnum = pointToPosition(x, y);
//
//                if (itemnum == AdapterView.INVALID_POSITION)
//                        break;                 
//                else{
//                	if(itemnum==0){
//                        if(itemnum==(getAdapter().getCount()-1)){                                    
//                            setSelector(R.drawable.app_list_corner_round);
//                        }else{
//                            setSelector(R.drawable.app_list_corner_round_top);
//                        }
//	                }else if(itemnum==(getAdapter().getCount()-1))
//	                        setSelector(R.drawable.app_list_corner_round_bottom);
//	                else{                            
//	                    setSelector(R.drawable.app_list_corner_shape);
//	                }
//                }
//                break;
//        case MotionEvent.ACTION_UP:
//        	System.out.println("MotionEvent.ACTION_UP===");
//            int ux = (int) ev.getX();
//            int uy = (int) ev.getY();
//            int u_itemnum = pointToPosition(ux, uy);
//
//            if (u_itemnum == AdapterView.INVALID_POSITION)
//            {
//            	System.out.println("MotionEvent.ACTION_UP===INVALID_POSITION");
//            	 break;
//            }
//                          
//            else{
//            	if(u_itemnum==0){
//                    if(u_itemnum==(getAdapter().getCount()-1)){                                    
//                        setSelector(R.drawable.app_list_corner_round_up);
//                    }else{
//                        setSelector(R.drawable.app_list_corner_round_top_up);
//                    }
//                }else if(u_itemnum==(getAdapter().getCount()-1))
//                        setSelector(R.drawable.app_list_corner_round_bottom_up);
//                else{                            
//                    setSelector(R.drawable.app_list_corner_shape_up);
//                }
//            }
//                break;
//        }
//        
////        return super.onInterceptTouchEvent(ev);
//        return false;
//    }
}

