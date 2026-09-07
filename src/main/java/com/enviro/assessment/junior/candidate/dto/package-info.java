/**
 * Data Transfer Objects used at the API boundary.
 *
 * request/ - objects the frontend sends to the API (validated with
 * Jakarta Bean Validation annotations).
 * response/ - objects the API sends back. Keeping these separate from
 * entities means we control exactly what fields are exposed and can
 * change the database model without breaking the API contract.
 */
package com.enviro.assessment.junior.candidate.dto;
