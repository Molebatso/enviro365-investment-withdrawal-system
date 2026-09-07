/**
 * Input-validation support beyond what standard Jakarta Bean Validation
 * annotations cover (e.g. custom constraint annotations), if needed.
 *
 * Note the distinction drawn throughout this project:
 *  - INPUT validation (is the request well-formed?) lives here / on DTOs.
 *  - BUSINESS RULE validation (is this withdrawal allowed?) lives in the
 *    service layer, because it depends on data from the database.
 */
package com.enviro.assessment.junior.candidate.validation;
