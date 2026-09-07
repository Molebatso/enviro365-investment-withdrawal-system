package com.enviro.assessment.junior.candidate.entity;

/**
 * The outcome of a withdrawal request.
 *
 * Per the assessment brief, invalid withdrawals are never persisted -
 * so in the current design only APPROVED rows end up in the database.
 * REJECTED is still modeled explicitly because it makes the domain
 * model self-documenting and leaves room to persist rejected attempts
 * later (e.g. for an audit trail) without changing the enum.
 */
public enum WithdrawalStatus {
    APPROVED,
    REJECTED
}
