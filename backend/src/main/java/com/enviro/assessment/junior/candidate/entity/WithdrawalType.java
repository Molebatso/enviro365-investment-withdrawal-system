package com.enviro.assessment.junior.candidate.entity;

/**
 * The type of withdrawal being requested.
 *
 * RETIREMENT withdrawals are subject to the age > 65 rule
 * (see WithdrawalService). STANDARD withdrawals are not age-restricted
 * but are still subject to the balance and 90%-cap rules, which apply
 * regardless of type.
 */
public enum WithdrawalType {
    RETIREMENT,
    STANDARD
}
