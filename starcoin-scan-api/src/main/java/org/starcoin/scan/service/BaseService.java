package org.starcoin.scan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.starcoin.constant.StarcoinNetwork;
import org.starcoin.scan.repository.BlockInfoRepository;
import org.starcoin.scan.repository.TransferJournalRepository;
import org.starcoin.scan.repository.barnard.BarnardBlockInfoRepository;
import org.starcoin.scan.repository.barnard.BarnardTransferJournalRepository;
import org.starcoin.scan.repository.halley.HalleyBlockInfoRepository;
import org.starcoin.scan.repository.halley.HalleyTransferJournalRepository;
import org.starcoin.scan.repository.main.MainBlockInfoRepository;
import org.starcoin.scan.repository.main.MainTransferJournalRepository;

@Service
public class BaseService {

    @Value("${indexer.version}")
    private String indexVersion;

    public String getIndex(String network, String indexConstant) {
        return network + indexVersion + "." + indexConstant;
    }

    @Autowired
    private MainBlockInfoRepository mainBlockInfoRepository;

    @Autowired
    private BarnardBlockInfoRepository barnardBlockInfoRepository;

    @Autowired
    private HalleyBlockInfoRepository halleyBlockInfoRepository;

    @Autowired
    private MainTransferJournalRepository mainTransferJournalRepository;
    @Autowired
    private BarnardTransferJournalRepository barnardTransferJournalRepository;
    @Autowired
    private HalleyTransferJournalRepository halleyTransferJournalRepository;

    BlockInfoRepository getBlockInfoRepository(String network) {
        StarcoinNetwork starcoinNetwork = StarcoinNetwork.fromValue(network);
        if (starcoinNetwork == StarcoinNetwork.barnard) {
            return barnardBlockInfoRepository;
        }
        else if (starcoinNetwork == StarcoinNetwork.main) {
            return mainBlockInfoRepository;
        }
        else if (starcoinNetwork == StarcoinNetwork.halley) {
            return halleyBlockInfoRepository;
        }
        return null;
    }
    TransferJournalRepository getTransferJournalRepository(String network) {
        StarcoinNetwork starcoinNetwork = StarcoinNetwork.fromValue(network);
        if (starcoinNetwork == StarcoinNetwork.barnard) {
            return barnardTransferJournalRepository;
        }
        else if (starcoinNetwork == StarcoinNetwork.main) {
            return mainTransferJournalRepository;
        }
        else if (starcoinNetwork == StarcoinNetwork.halley) {
            return halleyTransferJournalRepository;
        }
        return null;
    }

}
