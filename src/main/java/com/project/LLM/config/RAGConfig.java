package com.project.LLM.config;

import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RAGConfig {
	
	private final VectorStore vectorStore;
	
	public RAGConfig(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}
	
	public void LoadData() {
		List<Document> documents = createDocument();
		vectorStore.add(documents);
	}
	
	public List<Document> createDocument() {
		
		List<Document> documents = List.of(
                new Document(
                    """
                    Section: Remote Work & Equipment
                    Full-time employees receive a one-time remote home office stipend of $1,200 USD.
                    This covers ergonomic desks, chairs, external monitors, and accessories.
                    Equipment remains company property for the first 12 months of employment.
                    Expense requests must be submitted through Expensify within 30 days of hire.
                    """,
                    Map.of(
                        "category", "hr_policy",
                        "topic", "stipend",
                        "doc_id", "HR-STIPEND-2026",
                        "department", "human_resources"
                    )
                ),
                new Document(
                    """
                    Section: Paid Time Off (PTO) & Leave
                    Acme Corp operates on a Flexible Time Off (FTO) policy for salaried employees.
                    There is no strict cap, but managers require at least 2 weeks of advance notice
                    for absences longer than 5 consecutive business days.
                    Standard parental leave provides 16 weeks of 100% paid leave for both birthing
                    and non-birthing parents, usable within the first 12 months after birth or adoption.
                    """,
                    Map.of(
                        "category", "hr_policy",
                        "topic", "leave_pto",
                        "doc_id", "HR-LEAVE-2026",
                        "department", "human_resources"
                    )
                ),
                new Document(
                    """
                    Section: Incident Management & Escalation
                    Severity 1 (P1) incidents designate outages affecting >10% of active transactions.
                    The On-Call Lead must initiate an incident bridge within 5 minutes of PagerDuty trigger.
                    Status updates must be posted to status.acme-corp.com every 20 minutes until resolution.
                    A blameless post-mortem document must be drafted in Notion within 48 hours.
                    """,
                    Map.of(
                        "category", "engineering",
                        "topic", "sre_incidents",
                        "doc_id", "ENG-INC-001",
                        "department", "platform_engineering"
                    )
                ),
                new Document(
                    """
                    Section: Cloud Deployment & CI/CD
                    All production deployments require two peer approvals on GitHub pull requests.
                    Direct commits to main/master branches are blocked by branch protection rules.
                    Deployments are executed exclusively via ArgoCD pipelines with automated canary analysis.
                    Rollbacks trigger automatically if HTTP 5xx error rates exceed 0.5% over a 3-minute window.
                    """,
                    Map.of(
                        "category", "engineering",
                        "topic", "deployments",
                        "doc_id", "ENG-CICD-004",
                        "department", "platform_engineering"
                    )
                )
            );
		return documents;

        }

}
