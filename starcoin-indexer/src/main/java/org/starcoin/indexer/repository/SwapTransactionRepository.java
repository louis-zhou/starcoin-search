package org.starcoin.indexer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.starcoin.bean.SwapTransaction;
import org.starcoin.bean.SwapType;

import java.math.BigDecimal;
import java.util.List;

public interface SwapTransactionRepository extends JpaRepository<SwapTransaction, String> {
    List<SwapTransaction> findAllByTransactionHash(String transactionHash);
    List<SwapTransaction> findAllBySwapType(SwapType swapType);

    @Query(value = "select * from {h-domain}swap_transaction where ts between :start_time and :end_time ", nativeQuery = true)
    List<SwapTransaction> findSwapTransactionByTs(@Param("start_time") long startTime, @Param("end_time") long endTime);

    @Modifying
    @Transactional
    @Query(value = "update {h-domain}swap_transaction set total_value=:total_value where swap_seq=:swap_seq", nativeQuery = true)
    void updateTotalValue(@Param("total_value") BigDecimal totalValue, @Param("swap_seq") long swapSeq);

    @Modifying
    @Transactional
    @Query(value = "update {h-domain}swap_transaction set total_value=:total_value, amount_a=:amount_a, amount_b=:amount_b where swap_seq=:swap_seq", nativeQuery = true)
    void updateAmountAndTotal(@Param("amount_a") BigDecimal amountA, @Param("amount_b") BigDecimal amountB,
                      @Param("total_value") BigDecimal totalValue, @Param("swap_seq") long swapSeq);

    @Modifying
    @Transactional
    @Query(value = "update {h-domain}swap_transaction set amount_a=:amount_a, amount_b=:amount_b where swap_seq=:swap_seq", nativeQuery = true)
    void updateAmount(@Param("amount_a") BigDecimal amountA, @Param("amount_b") BigDecimal amountB, @Param("swap_seq") long swapSeq);

    @Query(value = "select sum(total_value) as volume, sum(amount_a) as volumeAmount  from {h-domain}swap_transaction where token_a = :token "
            + " and swap_type >= 0 and swap_type <= 3 and (ts between :start_time and :end_time)", nativeQuery = true)
    TokenVolumeDTO getVolumeByTokenA(@Param("token") String tokenA, @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Query(value = "select sum(total_value) as volume, sum(amount_b) as volumeAmount  from {h-domain}swap_transaction where token_b = :token "
            + "and swap_type >= 0 and swap_type <= 3 and (ts between :start_time and :end_time)", nativeQuery = true)
    TokenVolumeDTO getVolumeByTokenB(@Param("token") String tokenB, @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Query(value = "select sum(total_value) as volume, sum(amount_a) as volumeAmount  from {h-domain}swap_transaction where token_a = :tokenA "
            + "and token_b = :tokenB "
            + " and (swap_type = 0 or swap_type = 3) and (ts between :start_time and :end_time)", nativeQuery = true)
    TokenVolumeDTO getPoolVolumeA(@Param("tokenA") String tokenA, @Param("tokenB") String tokenB,
                                  @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Query(value = "select sum(total_value) as volume, sum(amount_b) as volumeAmount  from {h-domain}swap_transaction where token_b = :tokenA "
            + "and token_a = :tokenB "
            + " and (swap_type = 0 or swap_type = 3) and (ts between :start_time and :end_time)", nativeQuery = true)
    TokenVolumeDTO getPoolVolumeB(@Param("tokenA") String tokenA, @Param("tokenB") String tokenB,
                                  @Param("start_time") long startTime, @Param("end_time") long endTime);


}
