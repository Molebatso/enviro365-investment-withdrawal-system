/**
 * Service layer: where business rules live.
 *
 * Services coordinate repositories, calculate withdrawal balances,
 * enforce the withdrawal business rules (retirement age, balance limit,
 * 90% cap), and decide whether a withdrawal is approved or rejected.
 * This is the layer most heavily unit tested, since it holds the logic
 * the assessment is actually evaluating.
 */
package com.enviro.assessment.junior.candidate.service;
