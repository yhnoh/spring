package com.example.springbatchexample.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@XmlRootElement(name = "transaction")
public class Transaction {

    private long transactionId;

    private long accountId;

    private String description;

    private BigDecimal credit;

    private BigDecimal debit;

    private Date timestamp;


    public Transaction() {
    }

    public Transaction(long transactionId, long accountId, String description, BigDecimal credit, BigDecimal debit, Date timestamp) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.description = description;
        this.credit = credit;
        this.debit = debit;
        this.timestamp = timestamp;
    }

    @XmlJavaTypeAdapter(JaxbDateSerilaizer.class)
    public void setTimestamp(Date timestamp){
        this.timestamp = timestamp;
    }

    public BigDecimal getTransactionAmount(){
        if(credit != null){
            if(debit != null){
                return credit.add(debit);
            }else{
                return credit;
            }
        }
        else if(debit != null){
            return debit;
        }else {
            return new BigDecimal(0);
        }
    }
}
