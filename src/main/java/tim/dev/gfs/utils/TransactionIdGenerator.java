package tim.dev.gfs.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

@Component
public class TransactionIdGenerator {
	
	DataSource dataSource;
	
	TransactionIdGenerator(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public String generateId(String table_name, String prefix, String branchCode) throws Exception {
		
		String sql = """
				SELECT generate_transaction_id(?, ?, ?);
				""";
		
		try(Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){

			ps.setString(1, table_name);
			ps.setString(2, prefix);
			ps.setString(3, branchCode);
			
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return rs.getString(1);
				}
				return null;
			}
			
		}
	}
	
}