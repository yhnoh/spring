package com.example.springbatchitemreader.paging_item_reader;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomPagingItemReader extends AbstractPagingItemReader<Member> {

    private List<Member> members = new ArrayList<>();
    private int memberSize = 0;

    public CustomPagingItemReader() {
        super.setName(ClassUtils.getShortName(AbstractPagingItemReader.class));
    }

    public CustomPagingItemReader(int pageSize) {
        this();
        super.setPageSize(pageSize);
    }


    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        this.initMembers();
        memberSize = members.size();
    }

    /**
     * 회원 데이터 초기화
     */
    private void initMembers() {
        for (int i = 0; i < 5; i++) {
            int id = i + 1;
            Member member = new Member(id, "name" + id, id);
            members.add(member);
        }
    }

    @Override
    protected void doReadPage() {

        this.initResults();
        this.addResults();
    }

    /**
     * 페이징 데이터 초기화
     */
    private void initResults() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    /**
     * 페이징 데이터 추기
     */
    private void addResults() {
        int startPage = this.getPage() * this.getPageSize();

        for (int i = 0; i < this.getPageSize(); i++) {
            int index = startPage + i;
            if (index == memberSize) {
                break;
            }
            results.add(members.get(index));
        }
    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }
}
