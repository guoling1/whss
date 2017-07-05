package com.jkm.base.common.util;

public class PageUtils {
	private int currentPage;
	private int pageSize;
	private int totalPage;
	private int totalRecord;
	private int viewpagecount = 5;
	private int startPageIndex;
	private int endPageIndex;
	public int getStartPageIndex() {
		int startpage = currentPage  - (viewpagecount % 2 == 0 ? viewpagecount / 2 - 1 : viewpagecount / 2);  
        int endpage = currentPage + viewpagecount / 2;  
        if (startpage < 1) {  
            startpage = 1;  
            if (totalPage >= viewpagecount)  
                endpage = viewpagecount;  
            else  
                endpage = totalPage;  
        } 
        if (endpage > totalPage) {  
            endpage = totalPage;  
            if ((endpage - viewpagecount) > 0)  
                startpage = endpage - viewpagecount + 1;  
            else  
                startpage = 1;  
        }  
        startPageIndex = startpage;
		return startPageIndex;
	}
	public int getEndPageIndex() {
		int startpage = currentPage  - (viewpagecount % 2 == 0 ? viewpagecount / 2 - 1 : viewpagecount / 2);  
        int endpage = currentPage + viewpagecount / 2;  
        if (startpage < 1) {  
            startpage = 1;  
            if (totalPage >= viewpagecount)  
                endpage = viewpagecount;  
            else  
                endpage = totalPage;  
        } 
        if (endpage > totalPage) {  
            endpage = totalPage;  
            if ((endpage - viewpagecount) > 0)  
                startpage = endpage - viewpagecount + 1;  
            else  
                startpage = 1;  
        }  
        endPageIndex = endpage;
		return endPageIndex;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		int totalPage = totalRecord%getPageSize()==0?totalRecord/getPageSize():totalRecord/pageSize+1;
		this.setTotalPage(totalPage);
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
