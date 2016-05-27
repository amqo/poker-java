/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven.game;

import com.amqo.pokermaven.utils.ExceptionUtil;
import com.amqo.pokermaven.utils.TexasHoldEmUtil.BetCommandType;

/**
 *
 * @author alberto
 */
public class BetCommand {

    private final BetCommandType type;
    private long chips;

    public BetCommand(BetCommandType type, long chips) {
        ExceptionUtil.checkNullArgument(type, "type");
        ExceptionUtil.checkMinValueArgument(chips, 0L, "chips");
        this.type = type;
        this.chips = chips;
    }

    public BetCommand(BetCommandType type) {
        this(type, 0);
    }

    public BetCommandType getType() {
        return type;
    }

    public long getChips() {
        return chips;
    }

    public void setChips(long chips) {
        this.chips = chips;
    }
}
